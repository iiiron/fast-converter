package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.handler.AbstractConverterHandler;

import java.util.Date;

public class DateToTimestampConverter extends AbstractConverterHandler<Date, Long, Void> {
    @Override
    protected Long doConvert(Date value, Void context) {
        return value.getTime();
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
