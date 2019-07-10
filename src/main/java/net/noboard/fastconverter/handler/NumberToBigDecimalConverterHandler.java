package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.base.AbstractConverterHandler;

import java.math.BigDecimal;

public class NumberToBigDecimalConverterHandler extends AbstractConverterHandler<Object, BigDecimal> {

    private static NumberToStringConverterHandler numberToStringConverterHandler = new NumberToStringConverterHandler();

    @Override
    protected BigDecimal converting(Object value, String tip) throws ConvertException {
        return new BigDecimal(numberToStringConverterHandler.convert(value));
    }

    @Override
    public boolean supports(Object value) {
        return numberToStringConverterHandler.supports(value);
    }
}
