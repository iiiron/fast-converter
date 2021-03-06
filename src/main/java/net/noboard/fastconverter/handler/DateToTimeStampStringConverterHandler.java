package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.core.AbstractConverterHandler;

import java.util.Date;

/**
 * @author wanxm
 */
public class DateToTimeStampStringConverterHandler extends AbstractConverterHandler<Date, String> {

    public DateToTimeStampStringConverterHandler() {
        super("ms");
    }

    @Override
    protected String converting(Date value, String tip) {
        switch (tip.toLowerCase()) {
            case "ms":
                return String.valueOf((value).getTime());

            case "s":
                return String.valueOf(value.getTime() / 1000);

            default:
                throw new ConvertException(String.format("DateToTimeStampStringConverterHandler do not support tip '%s'", tip));
        }
    }

    @Override
    public boolean supports(Object value) {
        return value != null && Date.class.isAssignableFrom(value.getClass());
    }
}
