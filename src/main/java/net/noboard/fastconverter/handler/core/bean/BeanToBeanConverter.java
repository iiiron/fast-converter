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
import java.util.Map;

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

        BeanInfo beanT;
        try {
            beanT = Introspector.getBeanInfo(to.getClass());
        } catch (IntrospectionException e) {
            throw new ConvertException(e);
        }

        Map<String, Object> map = parse(from, group);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            for (PropertyDescriptor tD : beanT.getPropertyDescriptors()) {
                if (entry.getKey().toLowerCase().equals(tD.getName().toLowerCase())) {
                    try {
                        tD.getWriteMethod().invoke(to, entry.getValue());
                    } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                        throw new ConvertException(String.format("after convert, value of field '%s' in class %s is not match the field '%s' of target class %s",
                                entry.getKey(), from.getClass().getName(), tD.getName(), to.getClass().getName()));
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
