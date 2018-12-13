package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;

public class BooleanToStringConverterHandler extends AbstractConverterHandler<Boolean, String> {
    @Override
    protected String converting(Boolean value, String tip) throws ConvertException {
        return value.toString();
    }

    @Override
    public boolean supports(Boolean value) {
        return value != null;
    }
}
