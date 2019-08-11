package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.support.ConvertibleAnnotatedUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;

public class BeanToBeanConverter extends AbstractBeanConverter<Object, Object> {

    public BeanToBeanConverter(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    public Object convert(Object from, String group) throws ConvertException {
        ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(from.getClass(), group);
        Object to;
        String targetName = null;
        try {
            if (convertibleBean.targetClass() != Void.class) {
                targetName = convertibleBean.targetClass().getName();
                to = convertibleBean.targetClass().newInstance();
            } else {
                targetName = convertibleBean.targetName();
                to = Class.forName(convertibleBean.targetName()).newInstance();
            }
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            throw new ConvertException(String.format("the target class %s of @ConvertibleBean pointed is can not be implemented", targetName), e);
        }

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
                    try {
                        tD.getWriteMethod().invoke(to, getConvertedValue(fD.getReadMethod().invoke(from), field, group));
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
    public boolean supports(Object value, String group) {
        return true;
    }
}
