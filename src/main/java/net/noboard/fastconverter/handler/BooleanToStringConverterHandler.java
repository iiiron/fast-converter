package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.core.AbstractConverterHandler;

public class BooleanToStringConverterHandler extends AbstractConverterHandler<Boolean, String> {
    @Override
    protected String converting(Boolean value, String tip) throws ConvertException {
        return value.toString();
    }

    @Override
    public boolean supports(Object value) {
        return value != null && Boolean.class.isAssignableFrom(value.getClass());
    }
}
