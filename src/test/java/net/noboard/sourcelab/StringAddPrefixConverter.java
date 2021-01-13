package net.noboard.sourcelab;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.core.AbstractConverterHandler;

public class StringAddPrefixConverter extends AbstractConverterHandler<String, String> {
    @Override
    protected String converting(String value, String tip) throws ConvertException {
        return tip + value;
    }

    @Override
    public boolean supports(Object value, String tip) {
        return true;
    }
}
