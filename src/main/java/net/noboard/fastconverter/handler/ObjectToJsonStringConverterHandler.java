package net.noboard.fastconverter.handler;

import com.alibaba.fastjson.JSON;
import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

/**
 * @author wanxm
 */
public class ObjectToJsonStringConverterHandler extends AbstractFilterBaseConverterHandler<Object, String> {

    public ObjectToJsonStringConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected String converting(Object value, String tip) throws ConvertException  {
        Converter converter = new CommonFilterBaseConverterHandler(this.getConverterFilter());
        return JSON.toJSONString(converter.convert(value));
    }

    @Override
    public boolean supports(Object value) {
        return true;
    }
}
