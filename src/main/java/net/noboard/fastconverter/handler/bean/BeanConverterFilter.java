package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.ConverterFilter;

import java.util.function.Function;

public interface BeanConverterFilter {
    BeanConverter filter(Object value, String group);

    void setConverterFilter(ConverterFilter converterFilter);

    ConverterFilter getConverterFilter();

    BeanConverterFilter addFirst(BeanConverter converter);

    BeanConverterFilter addFirst(Function<ConverterFilter, BeanConverter> add);

    BeanConverterFilter addLast(BeanConverter converter);

    BeanConverterFilter addLast(Function<ConverterFilter, BeanConverter> add);
}
