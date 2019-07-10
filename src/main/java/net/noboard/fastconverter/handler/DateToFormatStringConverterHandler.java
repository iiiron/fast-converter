package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.handler.base.AbstractConverterHandler;

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
        return new SimpleDateFormat(tip).format(value);
    }

    @Override
    public boolean supports(Object value) {
        return value != null && Date.class.isAssignableFrom(value.getClass());
    }
}
