package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;

import java.math.BigDecimal;

/**
 * @author wanxm
 */
public class NumberToStringConverterHandler extends AbstractConverterHandler<Object, String, Void> {
    @Override
    protected String doConvert(Object value, Void context) {
        Class<?> clazz = value.getClass();
        if (BigDecimal.class.isAssignableFrom(clazz)
                || Integer.class.isAssignableFrom(clazz)
                || Double.class.isAssignableFrom(clazz)
                || Float.class.isAssignableFrom(clazz)
                || Long.class.isAssignableFrom(clazz)
                || Short.class.isAssignableFrom(clazz)
                || Byte.class.isAssignableFrom(clazz)) {
            return String.valueOf(value);
        }
        throw new ConvertException("the source value not is an number");
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
