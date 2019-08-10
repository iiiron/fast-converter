package net.noboard.fastconverter.handler.core;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.handler.support.ConvertibleAnnotatedUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;

public class ConvertibleBeanConverterHandler extends AbstractFilterBaseConverterHandler<Object, Object> {

    public ConvertibleBeanConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter, Convertible.defaultGroup);
    }

    public ConvertibleBeanConverterHandler(ConverterFilter converterFilter, String group) {
        super(converterFilter, group);
    }

    public Object convert(Object value, Class clazz) {
        return this.convert(value, clazz.getName());
    }

    @Override
    protected Object converting(Object value, String group) throws ConvertException {
        ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(value.getClass(), group);

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
