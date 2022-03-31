package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

public class StringToBooleanConverterHandler extends AbstractConverterHandler<String, Boolean, Void> {
    @Override
    protected Boolean doConvert(String value, Void context) {
        return Boolean.parseBoolean(value);
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
