package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

public class StringToLongConverterHandler extends AbstractConverterHandler<String, Long, Void> {
    @Override
    protected Long doConvert(String value, Void context) {
        return Long.parseLong(value);
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
