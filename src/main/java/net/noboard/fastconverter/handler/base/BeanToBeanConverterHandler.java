package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.filter.CommonSkipConverterFilter;
import net.noboard.fastconverter.handler.support.FieldConverterHandler;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * 将指定Bean对象转换为一个新类型的Bean
 * <p>
 * ConverterFilter不支持的域将照搬原值。如果不指定转换目标类型，则转换目标类型=被转换Bean的类型（转换过程
 * 会按照转换器一一进行）。
 *
 * <p>
 * 下面描述中，将待转换Bean叫做A，转换结果叫做B。
 * <p>
 * 该转换器并不要求A和B是相同的类型，由于转换器基于Bean中域的名字（非大小写敏感）来进行数据的映射，
 * 所以A,B不需要是相同的类型，转换器仅针对A，B中相同域名的域进行A->B的转换，转换过程可通过@FieldConverter
 * 注解干预，也可根据注册到ConverterFilter中的默认转换器来进行默认转换（域名相同，但未通过@FieldConverter
 * 指定转换器的域，会被默认转换器转换）。
 * <p>
 * 如果不指定转换结果类型，则B的类型将等同A的类型。在这种情况下不能将BeanToBeanConverterHandler的行为
 * 理解为浅复制或者深复制。BeanToBeanConverterHandler的行为受到ConverterFilter中注册的各种转换器的
 * 实现的影响。而这些转换器并不一定会在转换后返回新的对象（例如SkippingConverterHandler）。
 */
public class BeanToBeanConverterHandler extends AbstractFilterBaseConverterHandler<Object, Object> {

    private static FieldConverterHandler fieldConverterHandler = new FieldConverterHandler();

    private static BeanToBeanConverterHandler beanTransfer = null;

    /**
     * 一个专门用于递归复制Bean的BeanToBeanConverterHandler实例，它不会对数据做任何修改，除非
     * 你通过@FieldConverter指定了转换器。
     */
    public static BeanToBeanConverterHandler transfer() {
        if (beanTransfer == null) {
            ConverterFilter converterFilter = new CommonSkipConverterFilter();
            beanTransfer = new BeanToBeanConverterHandler(converterFilter);
            converterFilter.addLast(beanTransfer);
        }

        return beanTransfer;
    }

    /**
     * transfer的自定义版本，用户可以通过传递不同的ConverterFilter实例，来影响默认转换行为。
     * 需要注意的是，它会自动在转换链末尾添加BeanToBeanConverterHandler转换器（以实现递归的
     * Bean To Bean转换能力），所以你不必在ConverterFilter实例中手动添加。
     *
     * @param converterFilter
     * @return
     */
    public static BeanToBeanConverterHandler transferCustom(ConverterFilter converterFilter) {
        BeanToBeanConverterHandler converterHandler = new BeanToBeanConverterHandler(converterFilter);
        converterFilter.addLast(converterHandler);
        return converterHandler;
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter, ConvertBean.defaultGroup);
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter, String group) {
        super(converterFilter, group);
    }

    public Object convert(Object value, Class clazz) {
        return this.convert(value, clazz.getName());
    }

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        BeanInfo beanF, beanT;
        Object objF, objT;
        try {
            objF = value;
            objT = (tip == null || "".equals(tip)) ? value.getClass().newInstance() : Class.forName(tip).newInstance();
            beanF = Introspector.getBeanInfo(objF.getClass());
            beanT = Introspector.getBeanInfo(objT.getClass());
        } catch (IntrospectionException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new ConvertException("BeanInfo初始化错误，请检查Bean的申明是否正确", e);
        }

        for (PropertyDescriptor fD : beanF.getPropertyDescriptors()) {
            if ("class".equals(fD.getName())) {
                continue;
            }

            for (PropertyDescriptor tD : beanT.getPropertyDescriptors()) {
                if (fD.getName().toLowerCase().equals(tD.getName().toLowerCase())) {
                    try {
                        Field field = objF.getClass().getDeclaredField(fD.getName());
                        FieldConverter[] fieldConverters = field.getAnnotationsByType(FieldConverter.class);
                        Object r = fD.getReadMethod().invoke(objF);
                        if (fieldConverters != null && fieldConverters.length > 0) {
                            if (r != null) {
                                Object v = r;
                                for (FieldConverter fieldConverter : fieldConverters) {
                                    v = fieldConverterHandler.handler(fieldConverter, v);
                                }
                                try {
                                    tD.getWriteMethod().invoke(objT, v);
                                } catch (IllegalArgumentException e) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("[");
                                    Arrays.stream(fieldConverters).forEach(fieldConverter -> stringBuilder.append(fieldConverter.converter().getName()));
                                    stringBuilder.append("]");
                                    throw new ConvertException(
                                            MessageFormat.format("数据转换后类型不符合接收对象对应域的类型。域:{0},目标类型:{1},转换器:{2}",
                                                    tD.getName(), tD.getPropertyType().getName(), stringBuilder), e);
                                }
                            }
                        } else {
                            Converter converter = this.filter(r);
                            try {
                                if (converter == null) {
                                    tD.getWriteMethod().invoke(objT, r);
                                } else if (BeanToBeanConverterHandler.class.isAssignableFrom(converter.getClass())) {
                                    tD.getWriteMethod().invoke(objT, converter.convert(r, tD.getPropertyType().getName()));
                                } else {
                                    tD.getWriteMethod().invoke(objT, converter.convert(r));
                                }
                            } catch (ConvertException | IllegalArgumentException e) {
                                throw new ConvertException(MessageFormat.format("域:{0},类型:{1},转换器类型:{2} ---> {3}",
                                        fD.getName(), fD.getPropertyType().getName(), converter.getClass().getName(),
                                        e.getMessage()), e);
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new ConvertException(e);
                    } catch (NoSuchFieldException e) {
                        throw new ConvertException(
                                MessageFormat.format("BeanToBeanConverterHandler反射获取对象域时发生异常：NoSuchFieldException  {0}.{1}  通常是因为在转换器过滤器中没有添加对相关类的支持，导致某些非Bean的java类被错误的抛给了BeanToBeanConverterHandler去处理", objF.getClass().getName(), fD.getName()),
                                e);
                    }
                    break;
                }
            }
        }

        return objT;
    }

    @Override
    public boolean supports(Object value) {
        if (value == null) {
            return false;
        } else {
            return AnnotatedElementUtils.getMergedAnnotation(value.getClass(), Convertible.class) != null;
        }
    }

    protected Object converting(Object value, String tip, String a) throws ConvertException {
        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(value.getClass(),Convertible.class.getName());
        String group = annotationAttributes.getString("group");

        return null;
    }
}
