package net.noboard.fastconverter.handler;

import com.alibaba.fastjson.JSON;
import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.handler.base.AbstractFilterBaseConverterHandler;
import net.noboard.fastconverter.handler.base.BeanToMapConverterHandler;
import net.noboard.fastconverter.handler.base.CommonFilterBaseConverterHandler;

/**
 * @author wanxm
 */
public class ObjectToJsonStringConverterHandler extends AbstractFilterBaseConverterHandler<Object, String> {

    CommonFilterBaseConverterHandler converterHandler;

    public ObjectToJsonStringConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
        converterHandler = new CommonFilterBaseConverterHandler(converterFilter.addLast(BeanToMapConverterHandler::new));
    }

    @Override
    protected String converting(Object value, String tip) throws ConvertException  {
        return JSON.toJSONString(converterHandler.convert(value));
    }

    @Override
    public boolean supports(Object value) {
        return converterHandler.supports(value);
    }
}
