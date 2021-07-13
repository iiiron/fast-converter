package net.noboard.fastconverter.handler.auto;

import net.noboard.fastconverter.ConvertException;

import java.util.Date;

public class DateToLongSensingConverter extends AbstractSensingConverter {

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        return ((Date) value).getTime();
    }

    @Override
    protected boolean supports(Class<?> sourceClass, Class<?> targetClass) {
        return Date.class.isAssignableFrom(sourceClass) && Long.class.isAssignableFrom(targetClass);
    }
}
