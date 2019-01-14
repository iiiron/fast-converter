package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;

import java.math.BigDecimal;

public class NumberConverterHandler extends AbstractConverterHandler<Object, BigDecimal> {
    @Override
    protected BigDecimal converting(Object value, String tip) throws ConvertException {
        return new BigDecimal(value.toString());
    }

    @Override
    public boolean supports(Object value) {
        if (value == null) {
            return false;
        }

        return value.getClass().isAssignableFrom(BigDecimal.class)
                || value.getClass().isAssignableFrom(Integer.class)
                || value.getClass().isAssignableFrom(Double.class)
                || value.getClass().isAssignableFrom(Float.class)
                || value.getClass().isAssignableFrom(Long.class);
    }
}
