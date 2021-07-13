package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.AbstractConverterHandler;

import java.util.LinkedList;

public class PipelineConverterHandler extends AbstractConverterHandler<Object, Object> {

    LinkedList<Converter> converters = new LinkedList<>();

    public void pushConverter(Converter converter) {
        converters.add(converter);
    }

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        Object obj = value;
        for (Converter converter : converters) {
            if (!converter.supports(obj)) {
                throw new ConvertException(String.format(
                        "appear not supports value in PipelineConverterHandler on convert, value type is %s, current converter is %s",
                        value == null ? "null" : value.getClass().getName(),
                        converter.getClass().getName()));
            }
            obj = converter.convert(obj, tip);
        }
        return obj;
    }

    @Override
    public boolean supports(Object value, String tip) {
        if (converters == null || converters.size() < 1) {
            return false;
        }
        return converters.get(0).supports(value);
    }
}
