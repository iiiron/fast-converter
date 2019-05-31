package net.noboard.fastconverter.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wanxm
 */
public class DateToFormatStringConverterHandler extends AbstractConverterHandler<Date, String> {
    public DateToFormatStringConverterHandler() {
        super("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    protected String converting(Date value, String tip) {
        if (value == null) {
            return "";
        }
        return new SimpleDateFormat(tip).format(value);
    }

    @Override
    public boolean supports(Date value) {
        return true;
    }
}
