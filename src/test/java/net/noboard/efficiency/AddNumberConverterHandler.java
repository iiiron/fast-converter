package net.noboard.efficiency;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.core.AbstractConverterHandler;

public class AddNumberConverterHandler extends AbstractConverterHandler<String, String> {
    @Override
    protected String converting(String s, String s2) throws ConvertException {
        return s + "F";
    }

    @Override
    public boolean supports(Object o, String tip) {
        return true;
    }
}
