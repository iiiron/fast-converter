package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;
import net.noboard.fastconverter.ConvertException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wanxm
 */
public class FormatStringToDateConverterHandler extends AbstractConverterHandler<String, Date, String> {
    @Override
    protected Date doConvert(String value, String context) {
        try {
            return new SimpleDateFormat(context).parse(value);
        } catch (ParseException e) {
            throw new ConvertException("string to Date convert fail. + " + value + " is not match format " + context);
        }
    }

    @Override
    protected String defaultContext() {
        return "yyyy-MM-dd HH:mm:ss";
    }
}
