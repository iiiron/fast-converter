package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

public class StringToDoubleConverterHandler extends AbstractConverterHandler<String, Double, Void> {
    @Override
    protected Double doConvert(String value, Void context) {
        return Double.parseDouble(value);
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
