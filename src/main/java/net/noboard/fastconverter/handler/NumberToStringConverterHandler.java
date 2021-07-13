package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

import java.math.BigDecimal;

/**
 * @author wanxm
 */
public class NumberToStringConverterHandler extends AbstractConverterHandler<Object, String> {

    @Override
    protected String converting(Object value, String tip) {
        return String.valueOf(value);
    }

    @Override
    public boolean supports(Object value, String tip) {
        if (value == null) {
            return false;
        }

        Class clazz = value.getClass();

        return BigDecimal.class.isAssignableFrom(clazz)
                || Integer.class.isAssignableFrom(clazz)
                || Double.class.isAssignableFrom(clazz)
                || Float.class.isAssignableFrom(clazz)
                || Long.class.isAssignableFrom(clazz)
                || Short.class.isAssignableFrom(clazz)
                || Byte.class.isAssignableFrom(clazz);
    }
}
