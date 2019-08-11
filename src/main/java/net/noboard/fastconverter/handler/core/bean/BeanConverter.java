package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

public interface BeanConverter<T,K> {

    K convert(T value, String group) throws ConvertException;

    void setFilter(ConverterFilter converterFilter);

    ConverterFilter getFilter();

    boolean supports(Object value, String group);
}
