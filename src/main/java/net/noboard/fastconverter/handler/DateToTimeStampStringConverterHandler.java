package net.noboard.fastconverter.handler;

import java.util.Date;

/**
 * @author wanxm
 */
public class DateToTimeStampStringConverterHandler extends AbstractConverterHandler<Date, String> {
    @Override
    protected String converting(Date value, String tip) {
        return String.valueOf((value).getTime());
    }

    @Override
    public boolean supports(Object value) {
        return value != null && value.getClass().isAssignableFrom(Date.class);
    }
}
