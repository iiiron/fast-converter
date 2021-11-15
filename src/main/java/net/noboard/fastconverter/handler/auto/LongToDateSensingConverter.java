package net.noboard.fastconverter.handler.auto;

import java.util.Date;

public class LongToDateSensingConverter extends AbstractSensingConverter<Long, Date> {

    @Override
    protected Date converting(Long value, Class<Date> targetClass) {
        return new Date(value);
    }

    @Override
    protected boolean supports(Class<?> sourceClass, Class<?> targetClass) {
        return Long.class.isAssignableFrom(sourceClass) && Date.class.isAssignableFrom(targetClass);
    }
}
