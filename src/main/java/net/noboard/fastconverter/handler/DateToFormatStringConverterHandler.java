package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wanxm
 */
public class DateToFormatStringConverterHandler extends AbstractConverterHandler<Date, String, String> {
    @Override
    protected String doConvert(Date value, String context) {
        return new SimpleDateFormat(context).format(value);
    }

    @Override
    protected String defaultContext() {
        return "yyyy-MM-dd HH:mm:ss";
    }
}
