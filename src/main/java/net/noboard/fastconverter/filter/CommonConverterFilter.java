package net.noboard.fastconverter.filter;

import net.noboard.fastconverter.Converter;

import java.util.List;

public class CommonConverterFilter extends AbstractConverterFilter {
    @Override
    protected void initConverters(List<Converter> converters) {
        this.addContainerConverter(converters);
    }
}
