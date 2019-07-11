package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.base.AbstractConverterHandler;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Date;

public class SkippingConverterHandler extends AbstractConverterHandler<Object, Object> {

    private Class[] classes;

    /**
     * 8中基本数据类型的包装类 + String
     */
    public static Class[] BASIC_DATA_TYPE = {Integer.class, Double.class, Float.class,
            Long.class, Byte.class, Short.class, Character.class, String.class, Boolean.class};

    /**
     * 常见数据类型
     */
    public static Class[] COMMON_DATA_TYPE = {Date.class, BigDecimal.class};

    public SkippingConverterHandler(Class... classes) {
        this.classes = classes;
    }

    public SkippingConverterHandler() {

    }

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        return value;
    }

    @Override
    public boolean supports(Object value) {
        if (classes == null || Array.getLength(classes) == 0) {
            return true;
        } else {
            if (value == null) {
                return false;
            }
            for (Class clazz : classes) {
                if (clazz.isAssignableFrom(value.getClass())) {
                    return true;
                }
            }
            return false;
        }
    }
}
