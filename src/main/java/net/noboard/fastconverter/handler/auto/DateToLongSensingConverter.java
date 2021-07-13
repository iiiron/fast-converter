package net.noboard.fastconverter.handler.auto;

import net.noboard.fastconverter.ConvertException;

import java.util.Date;

public class LongToDateSensingConverter extends AbstractSensingConverter {

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        return new Date((Long) value);
    }

    @Override
    protected boolean supports(Class<?> sourceClass, Class<?> targetClass) {
        return sourceClass.isAssignableFrom(Long.class) && targetClass.isAssignableFrom(Date.class);
    }
}
