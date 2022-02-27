package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.handler.AbstractConverterHandler;

import java.util.Date;

public class TimestampToDateConverter extends AbstractConverterHandler<Long, Date, Void> {
    @Override
    protected Date doConvert(Long value, Void context) {
        return new Date(value);
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
