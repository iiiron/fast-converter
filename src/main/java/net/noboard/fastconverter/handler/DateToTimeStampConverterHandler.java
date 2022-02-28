package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

import java.util.Date;

/**
 * @author wanxm
 */
public class DateToTimeStampConverterHandler extends AbstractConverterHandler<Date, Long, Void> {

    @Override
    protected Long doConvert(Date value, Void context) {
        return value.getTime();
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
