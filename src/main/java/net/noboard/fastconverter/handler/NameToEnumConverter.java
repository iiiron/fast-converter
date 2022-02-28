package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

public class NameToEnumConverter extends AbstractConverterHandler<String, Enum, Class<Enum>> {
    @Override
    protected Enum doConvert(String value, Class<Enum> context) {
        return Enum.valueOf(context, value);
    }

    @Override
    protected Class<Enum> defaultContext() {
        return null;
    }
}
