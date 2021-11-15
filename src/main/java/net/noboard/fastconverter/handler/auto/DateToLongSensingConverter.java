package net.noboard.fastconverter.handler.auto;

import java.util.Date;

public class DateToLongSensingConverter extends AbstractSensingConverter<Date, Long> {

    @Override
    protected Long converting(Date value, Class<Long> targetClass) {
        return value.getTime();
    }

    @Override
    protected boolean supports(Class<?> sourceClass, Class<?> targetClass) {
        return Date.class.isAssignableFrom(sourceClass) && Long.class.isAssignableFrom(targetClass);
    }
}
