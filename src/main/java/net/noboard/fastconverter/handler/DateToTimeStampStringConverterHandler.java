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
    public boolean supports(Date value) {
        return value != null;
    }
}
