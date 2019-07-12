package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

import java.util.Map;


/**
 * Map转换器
 * <p>
 * 该转换器将根据原Map的实际类型生成一个新的相同类型的实例。并使用ConverterFilter中注册的转换器对
 * 原Map中的元素的value进行转换，将转换结果推入新的Map中（key不变，value为转换后的值）。如果转换器
 * 筛选器没有筛选出针对某一值的转换器，则将此原值推入新的Map中。
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
        Map<Object, K> newMap;
        try {
            newMap = value.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ConvertException("实例化" + value.getClass() + "失败", e);
        }
        for (Object key : value.keySet()) {
            Converter converter = this.getConverter(value.get(key));
            if (converter != null) {
                newMap.put(key, (K) converter.convert(value.get(key), tip));
            } else {
                newMap.put(key, (K) value.get(key));
            }
        }

        return newMap;
    }
}
