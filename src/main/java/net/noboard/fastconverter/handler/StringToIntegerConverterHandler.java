package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

public class StringToIntegerConverterHandler extends AbstractConverterHandler<String, Integer, Void> {
    @Override
    protected Integer doConvert(String value, Void context) {
        return Integer.parseInt(value);
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
