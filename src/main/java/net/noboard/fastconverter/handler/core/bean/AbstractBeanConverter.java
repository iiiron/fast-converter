package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.parser.ConvertibleMap;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
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


            ConvertibleMap currentMap = ConvertibleAnnotatedUtils.parse(field, group, group);
            ConvertibleMap last = lastConvertibleField(currentMap);
            String nameTo = fD.getName();
            if (last != null) {
                if (last.isAbandon()) {
                    continue;
                }
                if (last.getNameTo() != null && !"".equals(last.getNameTo())) {
                    nameTo = last.getNameTo();
                }
            }

            try {
                result.put(nameTo, getConvertedValue(fD.getReadMethod().invoke(from), currentMap));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ConvertException(e);
            }
        }

        return result;
    }

    private ConvertibleMap lastConvertibleField(ConvertibleMap convertibleMap) {
        if (convertibleMap == null) {
            return null;
        }
        ConvertibleMap currentMap = convertibleMap;
        while (currentMap.hasNext()) {
            currentMap = currentMap.next();
        }
        return currentMap;
    }

    private Object getConvertedValue(Object value, ConvertibleMap currentMap) {
        while (true) {
            if (value != null || !currentMap.isRetainNull()) {
                Converter converter = currentMap.getConverter();
                if (converter == null) {
                    converter = this.converterFilter.filter(value);
                }

                if (converter != null) {
                    if (Converter.isTipHasMessage(currentMap.getTip())) {
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
