package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractFilterBaseConverterHandler;
import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

import java.lang.reflect.Array;

/**
 * 数组到数组转换器
 * <p>
 * 该转换器会产生一个可以放置转换后数据的新数组，并使用ConverterFilter中注册的转换器对
 * 原数组中的元素一一进行转换，转换结果推入新数组中。如果过滤器没有找到可用的转换器则直接
 * 将原数据推入新数组中
 * <p>
 * 注意，由于java存在自动装箱，int[]经过转换后将变为Integer[]，其他基本数据类型同理。
 */
public class ArrayToArrayConverterHandler extends AbstractFilterBaseConverterHandler<Object, Object> {

    public ArrayToArrayConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        Converter converter;
        Object newArray = null, newV;

        for (int index = 0; index < Array.getLength(value); index++) {
            newV = Array.get(value, index);
            converter = this.filter(newV);
            if (converter != null) {
                newV = Converter.isTipHasMessage(tip) ? converter.convert(newV, tip) : converter.convert(newV);
            }
            if (newArray == null && newV != null) {
                newArray = Array.newInstance(newV.getClass(), Array.getLength(value));
            }
            if (newArray != null) {
                Array.set(newArray, index, newV);
            }
        }

        return newArray;
    }

    @Override
    public boolean supports(Object value, String tip) {
        return value != null && value.getClass().isArray();
    }
}
