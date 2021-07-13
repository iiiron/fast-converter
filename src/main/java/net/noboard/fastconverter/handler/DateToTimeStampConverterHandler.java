package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.AbstractConverterHandler;

import java.util.Date;

/**
 * @author wanxm
 */
public class DateToTimeStampConverterHandler extends AbstractConverterHandler<Date, Long> {

    public DateToTimeStampConverterHandler() {
        super("ms");
    }

    @Override
    protected Long converting(Date value, String tip) {
        switch (tip.toLowerCase()) {
            case "ms":
                return (value).getTime();

            case "s":
                return value.getTime() / 1000;

            default:
                throw new ConvertException(String.format("DateToTimeStampStringConverterHandler do not support tip '%s'", tip));
        }
    }

    @Override
    public boolean supports(Object value, String tip) {
        return value != null && Date.class.isAssignableFrom(value.getClass());
    }
}
