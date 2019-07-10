package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.handler.base.AbstractFilterBaseConverterHandler;

import java.util.Map;

/**
 * Map<Object, T> -> Map<String, K>
 *
 * @author wanxm
 */
public class MapToMapConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<Map<Object, T>, Map<Object, K>> {

    public MapToMapConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    public boolean supports(Object value) {
        return value != null && Map.class.isAssignableFrom(value.getClass());
    }

    @Override
    protected Map<Object, K> converting(Map<Object, T> value, String tip) throws ConvertException {
        Map<Object, K> newMap = null;
        try {
            newMap = value.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ConvertException("实例化" + value.getClass() + "失败");
        }
        for (Object key : value.keySet()) {
            Converter converter = this.getConverter(value.get(key));
            if (converter != null) {
                newMap.put(key, (K) converter.convert(value.get(key), tip));
            } else {
                throw new ConvertException("没有转换器可以处理" + value.get(key));
            }
        }

        return newMap;
    }
}
