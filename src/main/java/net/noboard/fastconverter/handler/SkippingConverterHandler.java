package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;

public class SkippingConverterHandler extends AbstractConverterHandler<Object, Object> {

    private Class[] classes;

    public static Class[] BASIC_DATA_TYPE = {Integer.class, Double.class, Float.class,
            Long.class, Byte.class, Short.class, Character.class, String.class, Boolean.class};

    public SkippingConverterHandler(Class... classes) {
        this.classes = classes;
    }

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        return value;
    }

    @Override
    public boolean supports(Object value) {
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
