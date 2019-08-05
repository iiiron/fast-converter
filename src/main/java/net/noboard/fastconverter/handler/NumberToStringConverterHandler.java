package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.handler.base.AbstractConverterHandler;

import javax.validation.constraints.Max;
import java.math.BigDecimal;

/**
 * @author wanxm
 */
public class NumberToStringConverterHandler extends AbstractConverterHandler<Object, String> {

    @Override
    @Max(10)
    protected String converting(@Max(10) Object value, String tip) {
        return String.valueOf(value);
    }

    @Override
    public boolean supports(Object value) {
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
