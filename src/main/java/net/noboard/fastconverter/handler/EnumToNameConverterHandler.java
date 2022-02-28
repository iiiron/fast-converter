package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

public class EnumToNameConverterHandler extends AbstractConverterHandler<Enum<?>, String, Void> {
    @Override
    protected String doConvert(Enum<?> value, Void context) {
        return value.name();
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
