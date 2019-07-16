# fast-converter

（大家可以通过测试来查看基本使用方法。测试用例中使用了lombok，所以请给你的IDE安装lombok插件）

3.0版的fast-converter和之前的版本有较大的区别，不兼容！不兼容！不兼容！

在fast-converter的整个架构中，将转换器区分为两类。一类是AbstractConverterHandler的实现；一类是AbstractFilterBaseConverterHandler的实现。

AbstractConverterHandler的实现可以单独工作，而AbstractFilterBaseConverterHandler的实现需要与ConverterFilter的实现配合工作。

AbstractConverterHandler的实现就不多说了，它就是很纯粹的一类转换器，进来A，返回B，整个转换过程由其自己实现。

而AbstractFilterBaseConverterHandler稍微复杂一些。但其复杂也不是它本身复杂，是由于它的灵活使其具有无限多可能性而造成的。ConverterFilter描述的是一种转换器过滤器，它可以从事先注册到它内部的一堆Converter中筛选出可以对某数据进行转换的一个。所以AbstractFilterBaseConverterHandler将不仅能转换自己所支持的数据，它还可以转换ConverterFilter所支持的数据。例如CommonSkipConverterFilter，它被注册了：基本数据类型转换器，Date，BigDecimal转换器（SkippingConverterHandler）；数组转换器（ArrayToArrayConverterHandler）；聚集转换器（CollectionToCollectionConverterHandler）；Map转换器（MapToMapConverterHandler）。你可以用它作为参数创建BeanToMapConverterHandler，该实例将可以处理某个bean中的基本数据类型域，Date域，BigDecimal域，数组域，Collection实现类的域，Map实现类的域。（但不能处理bean中的Bean域（嵌套Bean），因为你没有将BeanToMapConverterHandler注册到CommonConverterFilter中）

另外，CommonSkipConverterFilter在注册数组转换器（ArrayToArrayConverterHandler）；聚集转换器（CollectionToCollectionConverterHandler）；Map转换器（MapToMapConverterHandler）时，使用了其自身（this）作为参数。这意味着，提供给数组转换器（ArrayToArrayConverterHandler）的转换器过滤器（CommonConverterFilter）也具备数组转换器功能。由此数组转换器将具备嵌套处理的能力（数组中嵌套数组）。其他两个原理同此。

展开到更一般来说，任何AbstractFilterBaseConverterHandler的实现，可以通过在其所接收的转换器过滤器中添加自身，而获得嵌套处理的能力。例如BeanToBeanConverterHandler中的transfer方法就是通过这样的方式实现Bean的嵌套转换的。

    public static BeanToBeanConverterHandler transfer() {
        if (beanTransfer == null) {
            ConverterFilter converterFilter = new CommonSkipConverterFilter();
            beanTransfer = new BeanToBeanConverterHandler(converterFilter);
            converterFilter.addLast(beanTransfer);
        }

        return beanTransfer;
    }

**为了使fast-converter具有统一的模式，三个用以实现递归的ArrayToArrayConverterHandler，CollectionToCollectionConverterHandler，MapToMapConverterHandler，均有如下特性：当筛选器中没有对某个值的转换器时，使用原值填充新容器。**

# 各类必要性

## Converter接口的supports方法

supports方法具有存在必要性，否则ConverterFilter没有充分的手段对Converter进行筛选。supports方法的参数有三种可行的方案

    boolean supports(Class clazz)

    boolean supports(Object value)

    boolean supports(T value)

Class太狭窄，它会使得supports函数仅能从类型入手筛选被转换数据，而我想要的是，转换器可以根据具体的数据来工作。泛型则会导致ConverterFilter的实现必须使用try catch包裹supports方法，否则它会有抛出ClassCastException的危险。所以接收Object成为了比较合适的选择。

## BeanToMapConverterHandler

由于BeanToMapConverterHandler提供了@FieldConverter的解释器，这意味着，你可以通过该注解指定特定的Converter到域上，而这些Converter也可能在ConverterFilter中使用，我们将null也当成一种数据存在形式（这意味着你可以通过向ConverterFilter注册一个排位靠前的null值转换器而使得整个转换过程对null都有统一的处理方式，就像CommonConverterFilter所做的那样），使得普遍的Converter没有支持null值得必要（也就是说除了NullConverterHandler之外，其他的Converter其实并没有必要实现对null值的兼容，就像StringConverterHandler的supports函数一样，它并不支持null值）。

为了让不支持null值的Converter可以和@FieldConverter很好的配合，BeanToMapConverterHandler将自己对null值进行处理，以确保指定到域上的Converter不会因为域是一个null值而抛出异常。

具体的说，当BeanToMapConverterHandler处理一个通过@FieldConverter指定了特定转换器的域时，如果域的值为null，则BeanToMapConverterHandler将丢弃该域，在转换结果map中将不会出现与该域对应的key-value。