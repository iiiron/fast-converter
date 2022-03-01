package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

public class NameToEnumConverterHandler<T extends Enum<T>> extends AbstractConverterHandler<String, T, Class<T>> {
    @Override
    protected T doConvert(String value, Class<T> context) {
        return Enum.valueOf(context, value);
    }

    @Override
    protected Class<T> defaultContext() {
        return null;
    }
}
