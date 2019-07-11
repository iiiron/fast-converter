package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.support.FieldConverterHandler;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 将指定Bean对象转换为一个新的指定类型的Bean（深度复制）
 * <p>
 * 下面描述中，将待转换Bean叫做A，转换结果叫做B。
 * <p>
 * 该转换器并不要求A和B是相同的类型，由于转换器基于Bean中域的名字（非大小写敏感）来进行数据的映射，
 * 所以A,B不需要是相同的类型，转换器仅针对A，B中相同域名的域进行A->B的转换，转换过程可通过@FieldConverter
 * 注解干预，也可根据注册到ConverterFilter中的默认转换器来进行默认转换（域名相同，但未通过@FieldConverter
 * 指定转换器的域，会被默认转换器转换）。
 *
 * 由于从一个Bean转换到另一个Bean的转换过程存在较强的类型限制（较强的目的性），使用BeanToBeanConverterHandler
 * 时，必须为其提供目标类型信息。BeanToBeanConverterHandler有两种方式设置目标类型信息，其一是通过构造函数，其
 * 二是通过convert方法提供tip信息（用tip指定目标类的全限定类名）。但BeanToBeanConverterHandler在递归转换过程
 * 中具有自动探明目标类型的能力，也就是说，你仅需指定最外层的目标类型，当目标类中包含其他Bean时，转换器会自动适配
 * 并完成转换工作。
 *
 * 这也导致了一个问题，当你试图将BeanToBeanConverterHandler添加到ConverterFilter中，企图赋予一个转换
 * 链处理Bean的能力时，你并不能简单的 new BeanToBeanConverterHandler(converterFilter) ，因为这样构造出来的
 * BeanToBeanConverterHandler不知道要将它遇到的Bean转换成什么类型（这个问题有部分要归罪到java的泛型机制上），
 * 因为调用链上的其他转换器并不具备类似BeanToBeanConverterHandler的探明目标类型的机制，也不能要求它们具备
 * 这样的能力。为了让BeanToBeanConverterHandler的使用者认识到它的限制，我将其构造函数
 * BeanToBeanConverterHandler(ConverterFilter)设为了私有的，转而提供了两个静态方法，beanCopy和beanCopyCustom。
 * 方法比构造器更有具体性，由它来承载这些是更合适的选择。
 *
 * 谨记，只有BeanToBeanConverterHandler自身具备目标类型探明的能力
 */
public class BeanToBeanConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<T, K> {

    private static FieldConverterHandler fieldConverterHandler = new FieldConverterHandler();

    private static BeanToBeanConverterHandler beanCopy = null;

    private Class from = null;

    /**
     * 一个专门用于复制Bean的BeanToBeanConverterHandler实例，它不会对数据做任何修改，除非
     * 你通过@FieldConverter指定了转换器。由于该转换器使用了无参的构造函数实例化，它将无法
     * 处理方法convert(Object)，你必须使用其他重载方法明确指出转换的目标类型才可以。
     */
    public static BeanToBeanConverterHandler beanCopy() {
        if (beanCopy == null) {
            ConverterFilter converterFilter = new CommonConverterFilter();
            beanCopy = new BeanToBeanConverterHandler(converterFilter);
            converterFilter.addLast(beanCopy);
        }

        return beanCopy;
    }

    /**
     * 该方法返回的是beanCopy的定制版本
     *
     * beanCopyCustom会在用户提供的ConverterFilter末尾添加BeanToBeanConverterHandler实例来
     * 增强转换链。使其可以处理递归的Bean To Bean转换。
     *
     * 要注意，BeanToBeanConverterHandler的类型探查能力对泛型不起作用。换句话说，它无法在没有
     * tip的情况下对List,Map等类似的泛型容器中的元素进行转换。
     *
     * @param converterFilter
     * @return
     */
    public static BeanToBeanConverterHandler beanCopyCustom(ConverterFilter converterFilter) {
        BeanToBeanConverterHandler converterHandler = new BeanToBeanConverterHandler(converterFilter);
        converterFilter.addLast(converterHandler);
        return converterHandler;
    }

    private BeanToBeanConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter, String tip) {
        super(converterFilter, tip);
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter, Class to) {
        super(converterFilter, to.getName());
    }

    /**
     * 该构造函数同其他的区别在于，它接收一个标识beanFrom的类型的class对象参数，使用该
     * 构造函数实例化的BeanToBean转换器，会在验证中检查beanFrom的类型是否是该构造器
     * 参数类型的子类，本类或接口实现类，是则验证通过，否则验证不通过。
     *
     * @param converterFilter
     * @param from
     * @param to
     */
    public BeanToBeanConverterHandler(ConverterFilter converterFilter, Class from, Class to) {
        super(converterFilter, to.getName());
        this.from = from;
    }

    @Override
    public boolean supports(Object value) {
        if (this.from != null) {
            return value != null && value.getClass() == this.from;
        } else {
            return value != null;
        }
    }

    public K convert(T value, Class<K> clazz) {
        return this.convert(value, clazz.getName());
    }

    @Override
    public K convert(T value) throws ConvertException {
        if ("".equals(getDefaultTip())) {
            throw new ConvertException("缺少必要的目标类型信息。请检查构造器参数，或使用带tip参数的convert方法。");
        }
        return super.convert(value);
    }

    @Override
    protected K converting(T value, String tip) throws ConvertException {
        BeanInfo beanF, beanT;
        T objF;
        K objT;
        try {
            objF = value;
            objT = (K) Class.forName(tip).newInstance();
            beanF = Introspector.getBeanInfo(objF.getClass());
            beanT = Introspector.getBeanInfo(objT.getClass());
        } catch (IntrospectionException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new ConvertException("BeanInfo初始化错误，请检查Bean的申明是否正确" + e);
        }

        for (PropertyDescriptor fD : beanF.getPropertyDescriptors()) {
            if ("class".equals(fD.getName())) {
                continue;
            }

            for (PropertyDescriptor tD : beanT.getPropertyDescriptors()) {
                if (fD.getName().toLowerCase().equals(tD.getName().toLowerCase())) {
                    try {
                        Field field = objF.getClass().getDeclaredField(fD.getName());
                        FieldConverter fieldConverter = field.getAnnotation(FieldConverter.class);
                        Object r = fD.getReadMethod().invoke(objF);
                        if (fieldConverter != null) {
                            if (r != null) {
                                try {
                                    tD.getWriteMethod().invoke(objT, fieldConverterHandler.handler(fieldConverter, r));
                                } catch (IllegalArgumentException e) {
                                    throw new ConvertException("数据转换后类型不符合beanTo对应域的类型。  域:"
                                            + tD.getName() + "  目标类型:" + tD.getPropertyType().getName()
                                            + "  转换器:" + fieldConverter.converter().getName());
                                }
                            }
                        } else {
                            Converter converter = this.getConverter(r);
                            try {
                                if (converter == null) {
                                    throw new ConvertException("没有转换器可以处理数据类型：" + r.getClass().getName());
                                } else if (BeanToBeanConverterHandler.class.isAssignableFrom(converter.getClass())) {
                                    tD.getWriteMethod().invoke(objT, converter.convert(r, tD.getPropertyType().getName()));
                                } else {
                                    tD.getWriteMethod().invoke(objT, converter.convert(r));
                                }
                            } catch (ConvertException | IllegalArgumentException e) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("域:").append(fD.getName()).append("  类型:");
                                stringBuilder.append(fD.getPropertyType().getName());
                                if (converter != null) {
                                    stringBuilder.append("  转换器类型:").append(converter.getClass().getName());
                                }
                                stringBuilder.append(" ---> ").append(e.getMessage());
                                throw new ConvertException(stringBuilder.toString());
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new ConvertException(e);
                    } catch (NoSuchFieldException e) {
                        throw new ConvertException("");
                    }
                    break;
                }
            }
        }

        return objT;
    }
}
