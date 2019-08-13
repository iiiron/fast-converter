# fast-converter 是什么？

fast-converter是一个高效快速的POJO转换器，能在注解的驱动下对POJO进行灵活，便捷的转换。

java定义过一个规范用来描述一种数据和行为的结合体，它叫Bean。它的特点是提供无参构造函数，提供getter/setter方法，实现序列化接口；并且没有明确规定，但隐含的内容是它可包含业务逻辑。后来人们将编程活动中经常出现的一种仅用来充当数据结构，即具备无参构造函数，提供getter/setter方法，可序列化的型似Bean的对象称为POJO（Plain Ordinary Java Object）。

fast-converter的作用对象是内部具备相应成员变量的POJO。POJO的定义只提到提供getter/setter方法，但没有限制必须在内部申明此两种方法操作的成员变量。而fast-converter的注解机制依赖于成员变量的存在（当然这是一个需要改进的点，将在未来版本中解除这种限制）。

# fast-converter 的作用

开发中我们经常遇到不同系统间（无论是语言层面还是进程层面）交流时，存在口径不一，但差别却不是很大的情况。

例如Restful api的系统中，Controller将数据反回前端时，也许只是将某个Java内部的POJO翻译成JSON字符串，并伴随着某些Java的数据类型翻译成其他样式，如Date转换为特定格式的日期描述字符串；或者将所有以Bigdecimal形式存在的金额转换成带两位小数的字符串；又或者对某些数据打码，加前后缀等等。

例如系统内部调用时对方需要的数据结构仅是你所拥有数据结构的子集的变种。

# 如何使用 fast-converter

POJO->POJO 的转换示例：

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

准备两个POJO，我打算从Son，转换到Daughter，所以Son中使用注解标注了各种转换信息。然后：

    public static void main(String[] args) {
        Son son = new Son();
        son.setName("wanxiangming");
        son.setBirthday(new Date());
        son.setSex(true);

        Daughter daughter = (Daughter) FastConverter.autoConvert(son);
    }

使用断点查看，可知。daughter.name是字符串“wanxiangming”，daughter.birthday是字符串“2019.08”，daughter.sexy是Bigdecimal，值为1。

# fast-converter 文档

