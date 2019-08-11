package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public abstract class AbstractBeanConverter<T, K> implements BeanConverter<T, K> {

    private ConverterFilter converterFilter;

    public AbstractBeanConverter(ConverterFilter converterFilter) {
        this.converterFilter = converterFilter;
    }

    @Override
    public void setFilter(ConverterFilter converterFilter) {
        this.converterFilter = converterFilter;
    }

    @Override
    public ConverterFilter getFilter() {
        return this.converterFilter;
    }

    @Override
    public K convert(T value, String group) throws ConvertException {
        return null;
    }

    protected Map<String, Object> parse(Object from, String group) {
        BeanInfo beanF;
        try {
            beanF = Introspector.getBeanInfo(from.getClass());
        } catch (IntrospectionException e) {
            throw new ConvertException(e);
        }

        Map<String, Object> result = new HashMap<>();
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

            try {
                result.put(nameTo, getConvertedValue(fD.getReadMethod().invoke(from), field, group));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ConvertException(e);
            }
        }

        return result;
    }

    protected ConvertibleField lastConvertibleField(LinkedHashSet<ConvertibleField> convertibleFields) {
        if (convertibleFields == null || convertibleFields.size() < 1) {
            return null;
        }
        return (ConvertibleField) convertibleFields.toArray()[convertibleFields.size() - 1];
    }

    protected Object getConvertedValue(Object value, Field field, String group) {
        ConvertibleMap currentMap = ConvertibleAnnotatedUtils.parse(field, null, group);
        while (true) {
            if (value != null || !currentMap.isRetainNull()) {
                Converter converter = currentMap.getConverter();
                if (converter == null) {
                    converter = this.converterFilter.filter(value);
                }

                if (converter != null) {
                    if (currentMap.getTip() != null) {
                        value = converter.convert(value, currentMap.getTip());
                    } else {
                        value = converter.convert(value);
                    }
                }
            }

            if (currentMap.hasNext()) {
                currentMap = currentMap.next();
            } else {
                break;
            }
        }

        return value;
    }
}
