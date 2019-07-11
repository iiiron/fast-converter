package net.noboard.fastconverter.filter;

import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.handler.base.NullConverterHandler;
import net.noboard.fastconverter.handler.base.SkippingConverterHandler;

import java.util.List;

public class CommonConverterFilter extends AbstractConverterFilter {
    @Override
    protected void initConverters(List<Converter<?, ?>> converters) {
        converters.add(new NullConverterHandler("2"));
        converters.add(new SkippingConverterHandler(SkippingConverterHandler.BASIC_DATA_TYPE));
        converters.add(new SkippingConverterHandler(SkippingConverterHandler.COMMON_DATA_TYPE));
        this.addContainerConverter(converters);
    }
}
