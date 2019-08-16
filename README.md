# FastConverter 是什么？

FastConverter是一个高效快速的POJO转换器，能在注解的驱动下对POJO进行灵活，便捷的转换。

java定义过一个规范用来描述一种数据和行为的结合体，它叫Bean。它的特点是提供无参构造函数，提供getter/setter方法，实现序列化接口；并且没有明确规定，但隐含的内容是它可包含业务逻辑。后来人们将编程活动中经常出现的一种仅用来充当数据结构，即具备无参构造函数，提供getter/setter方法，可序列化的型似Bean的对象称为POJO（Plain Ordinary Java Object）。

FastConverter的作用对象是内部具备相应成员变量的POJO。POJO的定义只提到提供getter/setter方法，但没有限制必须在内部申明此两种方法操作的成员变量。而FastConverter的注解机制依赖于成员变量的存在（当然这是一个需要改进的点，将在未来版本中解除这种限制）。

# FastConverter 的作用

开发中我们经常遇到不同系统间（无论是语言层面还是进程层面）交流时，存在口径不一，但差别却不是很大的情况。

例如Restful api的系统中，Controller将数据反回前端时，也许只是将某个Java内部的POJO翻译成JSON字符串，并伴随着某些Java的数据类型翻译成其他样式，如Date转换为特定格式的日期描述字符串；或者将所有以Bigdecimal形式存在的金额转换成带两位小数的字符串；又或者对某些数据打码，加前后缀等等。

例如系统内部调用时对方需要的数据结构仅是你所拥有数据结构的子集的变种。

# 如何使用 FastConverter

POJO->POJO 的转换示例：

```java
@ConvertibleBean(targetClass = Daughter.class)
public class Son {
    @ConvertibleField(converter = AddNumberConverterHandler.class)
    private String name;

    @ConvertibleField(converter = DateToFormatStringConverterHandler.class, tip = "yyyy.MM")
    private Date birthday;

    @ConvertibleField(converter = BooleanToNumberConverterHandler.class)
    @ConvertibleField(converter = NumberToBigDecimalConverterHandler.class, nameTo = "sexy")
    private Boolean sex;

    ... 省略getter/setter方法
}
    
public class Daughter {
    private String name;

    private String birthday;

    private BigDecimal sexy;

    ... 省略getter/setter方法
}
```

准备两个POJO，我打算从Son，转换到Daughter，所以Son中使用注解标注了各种转换信息。然后：

```java
public static void main(String[] args) {
    Son son = new Son();
    son.setName("wanxiangming");
    son.setBirthday(new Date());
    son.setSex(true);

    Daughter daughter = (Daughter) FastConverter.autoConvert(son);
}
```

使用断点查看，可知。daughter.name是字符串“wanxiangming”，daughter.birthday是字符串“2019.08”，daughter.sexy是Bigdecimal，值为1。

# FastConverter 的基本结构

阐述FastConverter的基本结构有助于大家理解和使用，所以在此允我絮叨两句。

FastConverter的整体架构由两个关键接口构成，一是Converter，一是ConverterFilter。Converter描述的是一个可对特定数据进行转换的转换器；ConverterFilter描述的是一个转换器的过滤器（你可以将它理解为是一个转换器工厂），你可以将一系列的Converter注册到ConverterFilter中，随后将一个数据抛给ConverterFilter，它便会从一众已注在册的Converter中选取一个可以对该数据进行处理的Converter返还给你。

下面是两个接口的代码。

```java
public interface Converter<T, K> {

    K convert(T value, String tip) throws ConvertException;

    K convert(T value) throws ConvertException;

    void setDefaultTip(String tip);

    String getDefaultTip();

    boolean supports(Object value);
}

public interface ConverterFilter {
    
    Converter filter(Object value);

    ConverterFilter addFirst(Converter converter);

    ConverterFilter addFirst(Function<ConverterFilter,Converter> add);

    ConverterFilter addLast(Converter converter);

    ConverterFilter addLast(Function<ConverterFilter,Converter> add);
}
```

目前的架构中，转换器的优先级是通过类似遍历列表的方式实现的。接口ConverterFilter通过四个addXXX方法表达了这个含义。排序靠前的Converter将在过滤中拥有较高的优先级，当ConverterFilter发现某个靠前的Converter支持对value进行处理时，它便不会再询问后续的Converter了。应该注意到，Converter接口申明了一个supports方法。

并且，大家要注意到，由于ConverterFilter相当于一个工厂实例，某些Converter甚至可以通过持有一个ConverterFilter的实例来支持多种数据的转换工作。FastConverter的部分功能就是通过这种方式实现的，如果你阅读源代码，会发现里面有一个抽象类AbstractFilterBaseConverterHandler，它描述的就是一种专门通过持有ConverterFilter来完成自己工作的Converter（我会在后续的深入讲解部分章节讨论这个问题）。

综上，在**如何使用 FastConverter**一节你看到了这段代码：

```java
Daughter daughter = (Daughter) FastConverter.autoConvert(son);
```

其实，原理仅是FastConverter通过静态初始化代码块，初始化了4个构成FastConverter核心功能的Converter，并通过FastConverter#autoConvert方法去调用它们，而已。

# FastConverter 的核心功能

上一节提到，FastConverter的核心功能由4个Converter构成。下面我就来聊一聊这四个Converter。

这四个Converter全部是AbstractFilterBaseConverterHandler的子类，也就是说，它们全部通过携带ConverterFilter的实例来完成工作，并且它们所携带的是同一个ConverterFilter实例，所以它们对同一个数据的处理行为是完全一致的。下面我将举其中两例来说一说，届时你就会明白“它们对同一个数据的处理行为是完全一致的”这句话是什么意思了。

首先它们四个分别是：ArrayToArrayConverterHandler，CollectionToCollectionConverterHandler，MapToMapConverterHandler，ConvertibleBeanConverterHandler。看了名字估计你已然能够猜到它们是干什么的了。FastConverter显然是具备嵌套处理转换的能力的，而这个能力就是此4个核心转换器所赐予的。

第一个我来说说CollectionToCollectionConverterHandler，由它就概括了ArrayToArrayConverterHandler和MapToMapConverterHandler，因为它们3个逻辑相似。

### CollectionToCollectionConverterHandler

这个转换器用来处理java.util.Collection的子类，自然它就涵盖了List，Set，Queue等一众子接口。

它的处理逻辑非常简单，根据原Collection的实际类型新生成一个该类型的实例，如果生成失败，则降级为java.util.ArrayList，并打印警告信息。然后使用它所持有的ConverterFilter实例获取对容器中各个元素的可支持Converter来对元素进行转换操作。

这个过程有以下逻辑构成：

- 如果ConverterFilter中没有支持当前元素的转换器，则将元素直接推入新容器；
- 否则，如果CollectionToCollectionConverterHandler的转换过程被指定了一个不为空字符串，也不为null的tip，**则使用该tip指导ConverterFilter中筛选出的Converter的转换工作**（也就是调用 Converter#convert(T,String) 方法）；
- 否则，调用Converter的另一个重载方法Converter#convert(T)，即让转换器使用它自己的默认tip对数据进行转换。

应该注意到，CollectionToCollectionConverterHandler本身不对Collection的元素进行具体的转换，而是交给了ConverterFilter中所注册的一众转换器去完成。因此对数据的转换规则完全依赖于你注册到ConverterFilter中的转换器。而FastConverter入口类的静态初始化块将四个核心转换器全部添加到了该ConverterFilter中，也就是说无论是多少层的容器嵌套，它都可以处理。

### ConvertibleBeanConverterHandler

在**如何使用 FastConverter**一节我展示了FastConverter的基本用法，可以看到，在对POJO的转换处理中，我使用了大量的注解，而对这些注解的支持就来自于ConvertibleBeanConverterHandler。这个转换器描述的是一种能对被@ConvertibleBean标注的POJO进行处理的能力。它同样是一个AbstractFilterBaseConverterHandler的子类，并且它所依赖的ConverterFilter和其他三个核心转换器是一致的，是同一个实例（这就是整个FastConverter表现出转换一致性的原因）（开篇已经描述了Java Bean和POJO的区别，文本使用POJO一词来进行讲解，主要是为了强调它和Bean的不同之处，但在代码层面，Bean具有更高的抽象级别，而我也打算在后续更新中完全接纳Bean，而不仅仅是POJO，所以当下代码和文档的描述存在一些不和谐之处，还请谅解）。

与CollectionToCollectionConverterHandler等不一样的是，ConvertibleBeanConverterHandler在转换的过程中可以将POJO转换为任何实体（目前工具直接支持的是转换为另一个POJO，以及转换为HashMap，将在后续版本中添加更多原生支持，但目前你需要自己来实现相关功能，后文将会对如何添加自己的实现做介绍，届时你将会发现，要对FastConverter进行扩展，是多么的简单）。

转换过程以@ConvertibleBean和@ConvertibleField两个注解为指导进行。

**@ConvertibleBean提供了4个属性**：

- String group()

  标识一个分组，默认值是“default”

- String tip()

  tip是group的别名，因此它的含义也是标识一个分组。tip和Group可以任意存在，但如果两者都申明，则值必须相同。默认值是“default”

- String targetName()

  用来描述目标类的全限定类名，目标类即转换结果类型。

- Class targetClass()

  功能同targetName，只不过你可以用class的形式申明，而不必使用字符串。但两者只能同时存在其一。

比较重要的是group参数的理解。group是一种对转换器的分组手段，我们一定不希望一个POJO只能被转换为某一个特定的POJO，因此我们对转换行为进行分组，默认分组就是“default”。ConvertibleBeanConverterHandler本质上是一个Converter接口的子类，还记得这个接口里有一个Converter#convert(T value,String tip)方法吗？对于ConvertibleBeanConverterHandler来说，这个tip就是用来告诉它使用哪个分组（group）对POJO进行转换。@ConvertibleBean是一个可重复注解，所以某些POJO看起来会是下面这个样子：

```java
@ConvertibleBean(targetClass = Daughter.class)
@ConvertibleBean(targetClass = Map.class, group = "toMap")
public class Son {
	...
}
```

这意味着，当你调用Converter#convert(T value）方法时，它将使用默认分组，传入Son的实例即可转换为Daughter对象实例，当你调用Converter#convert(T value, String tip) 方法时，传入Son的实例，并使用“toMap”做第二个参数，转换结果将得到一个HashMap。

**@ConvertibleField提供了6个属性**：

- String group()

  标识从属的分组，默认“default”，可以使用逗号分割从属的多个分组。

- String tip()

  标识指导转换器的tip，默认空字符串，表示无信息tip，则转换器将使用自己默认的tip进行数据转换（或理解为使用自己默认的转换逻辑进行转换）。

- Class<? extends Converter> converter()

  标识此处使用某个特定的转换器进行数据转换，如果没有设置此属性，则使用持有的ConverterFilter实例中注册的转换器对字段进行转换。任何实现了net.noboard.fastconverter.Converter接口的类都可成为该属性的值（注意要提供无参构造函数），为了方便实现该接口，FastConverter提供了AbstractConverterHandler抽象类，推荐大家通过继承该抽象类来实现自己的转换器。

- boolean abandon()

  标识是否抛弃该字段，默认为false，如果设置为true，则转换过程将不会对该字段进行转换处理，也不会照搬原值，对于转换结果来说，这个字段消失了。**要注意**，如果存在对同一字段的多次转换，则只有最后一次转换的abandon属性生效。

- String nameTo()

  标识转换后该字段的名字，默认是空字符串，表示不进行特殊的名字映射。例如Son的sex和Daughter的sexy字段并不对应，但通过将Son的sex字段nameTo sexy，则sex字段可被正确的映射到sexy字段上。**要注意**，如果存在对同一字段的多次转换，则只有最后一次转换的nameTo属性生效。

- boolean retainNull()

  标识是否保留null值，默认true，这个属性主要影响的是@ConvertibleField指定了转换器的情况，转换器通常来讲是不处理null值的（当然你可以把这个转换器写成处理null值，但那样有破坏转换器抽象的嫌疑，如果你将null当作一类值，则这个转换器即处理了它想要支持的值，又处理了null值，它处理了两种类型的值），此时如果字段值是null，而转换器又不支持，就会产生冲突，因此引入一个单独的属性来控制这种问题。默认情况下它将保留字段的null值，而不会触发转换过程，也就不会引发冲突。

同样，group字段要注意，@ConvertibleField上的group表示它所从属的分组，从属的分组来自@ConvertibleBean。而@ConvertibleField上的group可以通过逗号分隔的形式，表达一个@ConvertibleField可以支持多个@ConvertibleBean。其次要注意，@ConvertibleField的tip并不是其group的别名，此处的tip将作为转换器的tip以左右转换器的行为。

举个例子：

```java
@ConvertibleBean(targetClass = Daughter.class)
@ConvertibleBean(targetClass = Map.class, group = "toMap")
public class Son {
    @ConvertibleField(group="default, toMap", converter = AddNumberConverterHandler.class)
    private String name;

    @ConvertibleField(converter = DateToFormatStringConverterHandler.class, tip = "yyyy.MM")
    private Date birthday;

    @ConvertibleField(group="default, toMap", converter = BooleanToNumberConverterHandler.class)
    @ConvertibleField(converter = NumberToBigDecimalConverterHandler.class, nameTo = "sexy")
    private Boolean sex;
    
    ... 省略getter/setter
}
```

其中，name字段标注了AddNumberConverterHandler转换器，无论group是“default”还是“toMap”它都支持。sex字段的BooleanToNumberConverterHandler转换器同上，但是sex字段的NumberToBigDecimalConverterHandler转换器只支持“default”分组。所以当Son被转换为Daughter时，sex会变成Bigdecimal类型的sexy字段，而当Son被转换为Map时，sex字段将会变成Map的值为Integer类型的1，key为字符串“sex”。

@ConvertibleField也是一个可重复注解，它不仅能用来标注不同分组，同一个分组也可以重复标注，由于java的重复注解是符合顺序结构的，在这里，你可以对同一个分组标注多次，转换过程将按照你的标注顺序进行，这样你就可以复用某些转换器，而不必为了完成某些组合的转换行为而去创建新的转换器了。**如何使用 FastConverter**一节对Son对象的sex字段的标注就是这种用法。

# 如何自定制FastConverter的默认转换行为

前面介绍了，FastConverter的4个核心转换器都依赖其持有的ConverterFilter实例，而FastConverter的入口类在静态初始化块中已经向该实例添加了4个核心转换器，所以初始情况下，FastConverter能够处理Collection，Map的子类，以及数组和被@ConvertibleBean标注的POJO。而又因为ArrayToArrayConverterHandler，CollectionToCollectionConverterHandler，MapToMapConverterHandler这三个转换器只是将对应容器拆开（实际上ConvertibleBeanConverterHandler也只是将POJO拆开，具体字段的转换也要交给ConverterFilter去选择转换器），选取ConverterFilter中的转换器对元素进行处理，此时ConverterFilter中只有四个核心转换器，故，它实际上不会对任何其他类型（其他类型指的是除Collection，Map的子类，以及数组和被@ConvertibleBean标注的POJO以外的类型）的数据进行转换，仅保留原值。

FastConverter入口类提供了customDefaultConverters方法对ConverterFilter实例进行自定制。

具体来看，该方法返回是一个ConverterFilter的实例，这个实例就是四个核心转换器所持有的实例，还记得ConverterFilter接口那四个addXXX方法吗，是的，你可以通过向ConverterFilter实例添加Converter的实例来定制整个FastConverter的默认转换行为。

举个例子：

```java
FastConverter.customDefaultConverters()
    .addFirst(new DateToTimeStampStringConverterHandler());
```

通过上例，我们向FastConverter添加了一个默认行为，该行为就是“将java.util.Date对象转换为时间戳字符串”。你可以预想一下会发生什么。

在没有添加这个默认转换器之前，当FastConverter遇到一个Date对象，它不是Collection，Map的子类，也不是数组，也没有被@ConvertibleBean标注，所以它会在转换过程中被原封不动的保留到转换结果中去。而现在，当FastConverter遇到Date对象时，它将会被转换为一个时间戳字符串。当然，如果它是某个POJO的字段，那么默认转换行为会被@ConvertibleField中指定的转换器替代（如果指定了的话）。

```java
List<Date> dates = new ArrayList<>();
Date date = new Date;
dates.add(date);

FastConverter.autoConvert(dates);// 结果是 ArrayList<String>，包含一个元素，即当前时间戳字符串
FastConverter.autoConvert(date);// 结果是 String 对象，即当前时间戳字符串
```

回忆一下Son POJO

```java
@ConvertibleBean(targetClass = Daughter.class)
@ConvertibleBean(targetClass = Map.class, group = "toMap")
public class Son {
    ... 省略

    @ConvertibleField(converter = DateToFormatStringConverterHandler.class, tip = "yyyy.MM")
    private Date birthday;
    
    ... 省略
```

该POJO中有一个Date类型的字段birthday。其上的注解显示，当分组为“default”时，birthday字段将被转换为格式化（“yyyy.MM”）的时间字符串。但如果分组为“toMap”，birthday字段并没有设定相应的@ConvertibleField，则birthday将会被交给默认的转换器去转换，此时默认转换器能对Date进行处理，它是DateToTimeStampStringConverterHandler，birthday将被转换为时间戳字符串。

```java
Son son = new Son();
son.setBirthday(new Date());

FastConverter.autoConvert(son,"toMap");// 结果是HashMap，其中有key为birthday，value为格式化时间字符串的entry。
```

还有，DateToTimeStampStringConverterHandler有两种模式，默认是毫秒，还可以是秒，通过tip为“ms”和“s”来控制。则：

```java
@ConvertibleBean(targetClass = Daughter.class)
@ConvertibleBean(targetClass = Map.class, group = "toMap")
public class Son {
    ... 省略

    @ConvertibleField(converter = DateToFormatStringConverterHandler.class, tip = "yyyy.MM")
    @ConvertibleField(group = "toMap", tip = "s")
    private Date birthday;
    
    ... 省略
```

是的，@ConvertibleField并不是只为了指定转换器而存在的，它没有任何一个属性是必须的，含义就是，所有属性都可以单独使用。

还有，对容器（Collection，Map的子类，及数组）转换器指定的tip会穿透容器，作用在对元素的转换过程中（请注意前文对CollectionToCollectionConverterHandler的阐述内容）。

```java
@ConvertibleBean(targetClass = BeanB.class)
public class BeanA {
    
    @ConvertibleField(nameTo = "daughters", tip = "toMap")
    private List<Son> sons;
    
    ... 省略
}
```

这里指定的 tip = "toMap"，将会作用到对Son的转换上去，由于Son是一个@ConvertibleBean标注的POJO，所以会由ConvertibleBeanConverterHandler对它进行转换，而该转换器将tip用作对group的选择，所以，sons将会被转换为 BeanB 中的 List\<Map\>  类型的 daughters 字段。

也许看这里的用例比较难懂，如果无法理解，那就去项目的test目录下转一转吧！

最后！很重要的，FastConverter入口类使用静态类变量来存储各种ConverterFilter，它应该在程序启动的时候进行自定制化，而在程序的运行过程中不应该再对其进行定制（或许在未来我会加入临时定制的机制，但在4.0.0版本，它还不具备这样的能力）。

# Bean 转换器的自定制

**一般来说，理解前文的阐述，就可以在几乎所有场景下使用FastConverter了，如果你的需求已经满足，无需更深层次的自定制，则此后的内容，无须理会。**

当然，如果你的项目对POJO的转换存在更多，更灵活，更奇特的要求，FastConverter提供了足够灵活的接口，抽象，以及入口让你来自定制自己的转换器。

### ConvertibleBeanConverterHandler

在对POJO的处理层面上，我使用了两个新的接口：BeanConverter，BeanConverterFilter。它们不仅名字和前文阐述的两个核心接口相似，连内容也特别相似。

```java
public interface BeanConverter<T, K> {

    K convert(T value, String group) throws ConvertException;

    void setFilter(ConverterFilter converterFilter);

    ConverterFilter getFilter();

    boolean supports(Object value, String group);
}

public interface BeanConverterFilter {
    BeanConverter filter(Object value, String group);

    void setConverterFilter(ConverterFilter converterFilter);

    ConverterFilter getConverterFilter();

    BeanConverterFilter addFirst(BeanConverter converter);

    BeanConverterFilter addFirst(Function<ConverterFilter, BeanConverter> add);

    BeanConverterFilter addLast(BeanConverter converter);

    BeanConverterFilter addLast(Function<ConverterFilter, BeanConverter> add);
}
```

唯一的区别是，多了group。由于普通的转换器没有分组的概念，而在对POJO进行处理的过程中，需要有分组的支撑，尽管区别不大，但在抽象上还是有别的。它们和前文的Converter及ConverterFilter功能一样，一个是转换器，一个是过滤器（即工厂）。

还记得前文提到的4个核心转换器中的ConvertibleBeanConverterHandler吗？我介绍了它的使用，和它支撑的两个注解@ConvertibleBean和@ConvertibleFiled，但这两个注解都不是它直接提供支撑的，提供支撑的是BeanToBeanConverter和BeanToMapConverter（目前来说是这两个）。

再具体来说，也不是BeanToBeanConverter和BeanToMapConverter提供支撑，而是它们的父类AbstractBeanConverter提供的，所以接下来我说一说，AbstractBeanConverter是如何支撑两个注解的运作的，以及你可以如何扩展，如何自定制自己的AbstractBeanConverter子类。

### AbstractBeanConverter

```java
public abstract class AbstractBeanConverter<T, K> implements BeanConverter<T, K> {

    protected Map<String, Object> parse(Object from, String group) {
        ... 省略方法体
    }

    ... 省略其他方法和域
}

```

AbstractBeanConverter提供了一个parse方法，该方法可以根据POJO中的注解，将POJO解析成一个Map，BeanToBeanConverter的实现就是简单的将这个Map重新组装为POJO，而BeanToMapConverter的实现就更简单，它直接返回了这个Map。

如果你想构建自己的AbstractBeanConverter子类，而且根据AbstractBeanConverter#parse方法为你解析的Map就可以实现你的需求，那么了解到这里就足够了。

如果你连这个Map都无法使用，换句话说你必须重载这个方法，那么我需要向你介绍另一个东西：

```java
public interface ConvertibleParser {
    /**
     * @param annotatedElement 该次解析的 AnnotatedElement
     * @param group            该次解析的指定分组(即有效分组)
     * @return
     */
    ConvertibleMap parse(AnnotatedElement annotatedElement, String group);
}
```

以及，必须要贴出@ConvertibleField的完整代码

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ImportParser(clazz = ConvertibleFieldParser.class) // 注意这行代码
@Repeatable(ConvertibleFields.class)
@Convertible
public @interface ConvertibleField {
    @AliasFor(annotation = Convertible.class)
    String group() default Converter.DEFAULT_GROUP;

    @AliasFor(annotation = Convertible.class)
    String tip() default Converter.DEFAULT_TIP;

    @AliasFor(annotation = Convertible.class)
    Class<? extends Converter> converter() default Converter.class;

    boolean abandon() default false;

    String nameTo() default "";

    boolean retainNull() default true;
}
```

ConvertibleField使用了元注解@ImportParser，@ImportParser也是fast-converter内部提供的，它用来将一个解析器绑定到注解上，这样在使用注解的时候，就可以直接使用解析器来将注解解析为ConvertibleMap对象，ConvertibleMap是一个存储了转换过程中需要的各类信息的一个“转换地图”。

```java
public interface ConvertibleMap {
    void setAbandon(boolean abandon);

    boolean isAbandon();

    void setRetainNull(boolean isRetainNull);

    boolean isRetainNull();

    void setTip(String tip);

    String getTip();

    void setNameTo(String nameTo);

    String getNameTo();

    void setConverter(Converter converter);

    Converter getConverter();

    void join(ConvertibleMap convertibleMap);

    boolean hasNext();

    ConvertibleMap next();
}
```

如你所见，它提供了一系列信息来指导转换过程，而且，它是一个链表，这意味着转换过程可以串联起来。我已经提供了工具类ConvertibleAnnotatedUtils来完成这个解析的任务，在AbstractBeanConverter#parse中有如下这段代码：

```java
public abstract class AbstractBeanConverter<T, K> implements BeanConverter<T, K> {

    protected Map<String, Object> parse(Object from, String group) {
        ... 省略
            
        ConvertibleMap currentMap = ConvertibleAnnotatedUtils.parse(field, group);
        
        ... 省略
    }

    ... 省略其他方法和域
}
```

这样，我们就根据ConvertibleFieldParser的逻辑将字段上@ConvertibleField注解的信息解析成一个用于指导转换过程的ConvertibleMap。（这意味着，你可以编写自己的注解，自己的注解解析器，而它们可以和现有的系统契合，而不需要做更多额外的工作）

介绍这些是因为，如果你重写AbstractBeanConverter#parse方法，你需要知道这些内部的知识。当你重写时，自然你可以不遵循ConvertibleMap这种模式，但如果你不遵循，则你实现的AbstractBeanConverter子类将可能和其他任何该类的子类无法兼容，则你的Bean转换器将和你的一整套逻辑紧紧的绑定在一起。故推荐使用这种方式。

### 如何将自定制的Bean转换器添加到FastConverter中去

FastConverter入口类提供了FastConverter#customBeanConverters方法将你自己的Bean转换器添加到ConvertibleBeanConverterHandler中去，则一切被@ConvertibleBean标注的POJO，在被转换的时候就会考虑到你的自定义实现，如果你的supports成立，POJO就会被交给你的转换器去完成转换工作。



欢迎PR，以及我的邮箱[1062495630@qq.com]，欢迎交流。

