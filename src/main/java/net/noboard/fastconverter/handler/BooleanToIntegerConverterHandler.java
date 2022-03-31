package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

public class BooleanToIntegerConverterHandler extends AbstractConverterHandler<Boolean, Integer, Void> {
    @Override
    protected Integer doConvert(Boolean value, Void context) {
        return value ? 1 : 0;
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
