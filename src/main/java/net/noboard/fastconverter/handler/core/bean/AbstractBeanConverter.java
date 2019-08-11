package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.ConvertibleMap;
import net.noboard.fastconverter.handler.support.ConvertibleAnnotatedUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;

public abstract class AbstractBeanConverter<T,K> implements BeanConverter<T,K> {

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

    protected ConvertibleField lastConvertibleField(LinkedHashSet<ConvertibleField> convertibleFields) {
        if (convertibleFields == null || convertibleFields.size() < 1) {
            return null;
        }
        return (ConvertibleField) convertibleFields.toArray()[convertibleFields.size() - 1];
    }

    protected Object getConvertedValue(Object value, Field field, String group) {
        ConvertibleMap currentMap = ConvertibleAnnotatedUtils.parse(field, null, group);
        while (true) {
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

            if (currentMap.hasNext()) {
                currentMap = currentMap.next();
            } else {
                break;
            }
        }

        return value;
    }
}
