package net.noboard.fastconverter.handler;

import com.fuqin.base.converter.fastconverter.ConvertException;
import com.fuqin.base.converter.fastconverter.Converter;
import com.fuqin.base.converter.fastconverter.ConverterFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * Map<Object, T> -> Map<String, K>
 *
 * @author wanxm
 */
public class MapToMapConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<Map<Object, T>, Map<String, K>> {

    public MapToMapConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    public boolean supports(Map<Object, T> value) {
        return value != null;
    }

    @Override
    protected Map<String, K> converting(Map<Object, T> value, String tip) throws ConvertException {
        Map<String, K> newMap = new HashMap<>();
        for (Object key : value.keySet()) {
            Converter converter = this.getConverter(value.get(key));
            if (converter != null) {
                newMap.put(key.toString(), (K) converter.convert(value.get(key), tip));
            } else {
                throw new ConvertException("没有转换器可以处理" + value.get(key));
            }
        }

        return newMap;
    }
}
