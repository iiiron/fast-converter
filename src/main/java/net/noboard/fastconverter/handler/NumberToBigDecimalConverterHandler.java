package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

import java.math.BigDecimal;

public class NumberToBigDecimalConverterHandler extends AbstractConverterHandler<Object, BigDecimal, Void> {

    private static NumberToStringConverterHandler numberToStringConverterHandler = new NumberToStringConverterHandler();

    @Override
    protected BigDecimal doConvert(Object value, Void context) {
        return new BigDecimal(numberToStringConverterHandler.convert(value));
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
