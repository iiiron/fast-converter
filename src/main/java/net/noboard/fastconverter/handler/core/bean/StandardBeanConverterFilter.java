package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.ConverterFilter;

import java.util.List;

public class StandardBeanConverterFilter extends AbstractBeanConverterFilter {
    StandardBeanConverterFilter(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected void initConverters(List<BeanConverter> converters) {

    }
}
