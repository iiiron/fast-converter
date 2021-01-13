package net.noboard.fastconverter.handler.core;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

import java.util.HashMap;
import java.util.Map;


/**
 * Map转换器
 * <p>
 * 该转换器将根据原Map的实际类型生成一个新的相同类型的实例。并使用ConverterFilter中注册的转换器对
 * 原Map中的元素的value进行转换，将转换结果推入新的Map中（key不变，value为转换后的值）。如果转换器
 * 筛选器没有筛选出针对某一值的转换器，则将此原值推入新的Map中。
 *
 * 新容器生成失败时，将把新容器降级为HashMap，并输出警告信息
 */
public class MapToMapConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<Map<Object, T>, Map<Object, K>> {

    public MapToMapConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    public boolean supports(Object value, String tip) {
        return value != null && Map.class.isAssignableFrom(value.getClass());
    }

    @Override
    protected Map<Object, K> converting(Map<Object, T> value, String tip) throws ConvertException {
        Map<Object, K> newMap;
        try {
            newMap = value.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            newMap = new HashMap<>();
            System.err.println(String.format("class %s can't be implemented, the degradation measures implement the container with %s ",
                    value.getClass(), newMap.getClass()));
        }

        Converter converter;
        Object newV, oldV;
        for (Map.Entry entry : value.entrySet()) {
            converter = this.filter(entry.getValue());
            oldV = entry.getValue();
            if (converter == null) {
                newV = oldV;
            } else if (Converter.isTipHasMessage(tip)) {
                newV = converter.convert(oldV, tip);
            } else {
                newV = converter.convert(oldV);
            }
            newMap.put(entry.getKey(), (K) newV);
        }

        return newMap;
    }
}
