package net.noboard.fastconverter.handler;

import java.math.BigDecimal;

/**
 * @author wanxm
 */
public class NumberToStringConverterHandler extends AbstractConverterHandler<Object, String> {

    @Override
    protected String converting(Object value, String tip) {
        return value.toString();
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
