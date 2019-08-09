package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.filter.CommonSkipConverterFilter;
import net.noboard.fastconverter.handler.support.ConvertibleAnnotatedUtils;
import net.noboard.fastconverter.handler.support.FieldConverterHandler;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;

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

    public BeanToBeanConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter, Convertible.defaultGroup);
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter, String group) {
        super(converterFilter, group);
    }

    public Object convert(Object value, Class clazz) {
        return this.convert(value, clazz.getName());
    }

    @Override
    protected Object converting(Object value, String group) throws ConvertException {
        ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(value.getClass(), group);
        if (convertibleBean == null) {
            throw new ConvertException(String.format("no @ConvertibleBean annotation agree with group '%s', on bean %s",
                    group,
                    value.getClass()));
        }

        Object objT;
        String targetName = null;
        try {
            if (convertibleBean.targetClass() != Void.class) {
                targetName = convertibleBean.targetClass().getName();
                objT = convertibleBean.targetClass().newInstance();
            } else {
                targetName = convertibleBean.targetName();
                objT = Class.forName(convertibleBean.targetName()).newInstance();
            }
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            throw new ConvertException(String.format("the target class %s of @ConvertibleBean pointed is can not be implemented", targetName), e);
        }

        return convert(value, objT, group);
    }

    private Object convert(Object from, Object to, String group) {
        BeanInfo beanF, beanT;
        try {
            beanF = Introspector.getBeanInfo(from.getClass());
            beanT = Introspector.getBeanInfo(to.getClass());
        } catch (IntrospectionException e) {
            throw new ConvertException(e);
        }

        for (PropertyDescriptor fD : beanF.getPropertyDescriptors()) {
            if ("class".equals(fD.getName())) {
                continue;
            }

            Field field;
            try {
                field = from.getClass().getDeclaredField(fD.getName());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new ConvertException(String.format("field named '%s' not exist in %s",
                        fD.getName(),
                        from.getClass().getName()));
            }

            LinkedHashSet<ConvertibleField> convertibleFields = ConvertibleAnnotatedUtils.getMergedConvertField(field, group);
            ConvertibleField last = lastConvertibleField(convertibleFields);
            String nameTo = fD.getName();
            if (last != null) {
                if (last.abandon()) {
                    continue;
                }
                if (!"".equals(last.nameTo())) {
                    nameTo = last.nameTo();
                }
            }

            for (PropertyDescriptor tD : beanT.getPropertyDescriptors()) {
                if (nameTo.toLowerCase().equals(tD.getName().toLowerCase())) {
                    Object r = null;
                    try {
                        r = fD.getReadMethod().invoke(from);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new ConvertException(e);
                    }

                    ConvertibleMap currentMap = ConvertibleAnnotatedUtils.parse(field, null, group);
                    while (true) {
                        Converter converter = currentMap.getConverter();
                        if (converter == null) {
                            converter = this.filter(r);
                        }

                        if (converter != null) {
                            if (currentMap.getTip() != null) {
                                r = converter.convert(r, currentMap.getTip());
                            } else {
                                r = converter.convert(r);
                            }
                        }

                        if (currentMap.hasNext()) {
                            currentMap = currentMap.next();
                        } else {
                            break;
                        }
                    }

                    try {
                        tD.getWriteMethod().invoke(to, r);
                    } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                        throw new ConvertException(String.format("after convert, value of field '%s' in class %s is not match the field '%s' of target class %s",
                                fD.getName(), from.getClass().getName(), tD.getName(), to.getClass().getName()));
                    }

                    break;
                }
            }
        }

        return to;
    }

    @Override
    public boolean supports(Object value) {
        if (value == null) {
            return false;
        } else {
            return AnnotatedElementUtils.getMergedAnnotation(value.getClass(), ConvertibleBean.class) != null
                    || AnnotatedElementUtils.getMergedAnnotationAttributes(value.getClass(), ConvertibleBeans.class) != null;
        }
    }

    private ConvertibleField lastConvertibleField(LinkedHashSet<ConvertibleField> convertibleFields) {
        if (convertibleFields == null || convertibleFields.size() < 1) {
            return null;
        }
        return (ConvertibleField) convertibleFields.toArray()[convertibleFields.size() - 1];
    }
}
