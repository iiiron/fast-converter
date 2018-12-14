package net.noboard.fastconverter.filter;

import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.handler.*;

import java.util.List;

public class CommonConverterFilter extends AbstractConverterFilter {
    @Override
    protected void initConverters(List<Converter<?, ?>> converters) {
        converters.add(new StringConverterHandler());
        converters.add(new NumberToStringConverterHandler());
        converters.add(new BooleanToStringConverterHandler());
        converters.add(new DateToTimeStampStringConverterHandler());
        converters.add(new NullToEmptyStringConverterHandler());

        converters.add(new MapToMapConverterHandler<>(this));
        converters.add(new ArrayToListConverterHandler<>(this));
        converters.add(new CollectionToListConverterHandler<>(this));
        converters.add(new BeanToMapConverterHandler(this));
    }
}
