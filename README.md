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
- 否则，如果CollectionToCollectionConverterHandler的转换过程被指定了一个不为空字符串，也不为null的tip，则使用该tip指导ConverterFilter中筛选出的Converter的转换工作（也就是调用 Converter#convert(T,String) 方法）；
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

  标识此处使用某个特定的转换器进行数据转换，如果没有设置此属性，则使用持有的ConverterFilter实例中注册的转换器对字段进行转换。

- boolean abandon()

  标识是否抛弃该字段，默认为false，如果设置为true，则转换过程将不会对该字段进行转换处理，也不会照搬原值，对于转换结果来说，这个字段消失了。

- String nameTo()

  标识转换后该字段的名字，默认是空字符串，表示不进行特殊的名字映射。例如Son的sex和Daughter的sexy字段并不对应，但通过将Son的sex字段nameTo sexy，则sex字段可被正确的映射到sexy字段上。

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
}
```

其中，name字段标注了AddNumberConverterHandler转换器，无论group是“default”还是“toMap”它都支持。sex字段的BooleanToNumberConverterHandler转换器同上，但是sex字段的NumberToBigDecimalConverterHandler转换器只支持“default”分组。所以当Son被转换为Daughter时，sex会变成Bigdecimal类型的sexy字段，而当Son被转换为Map时，sex字段将会变成Map的值为Integer类型的1，key为字符串“sex”。

