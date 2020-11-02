package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.core.AbstractConverterHandler;

public class EnumToNameConverterHandler extends AbstractConverterHandler<Enum, String> {
    @Override
    protected String converting(Enum value, String tip) throws ConvertException {
        return value.name();
    }

    @Override
    public boolean supports(Object value) {
        return false;
    }
}
