package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

import java.lang.reflect.Array;
import java.text.MessageFormat;

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
        Object newArray = null;
        for (int index = 0; index < Array.getLength(value); index++) {
            Object obj = Array.get(value, index);
            converter = this.getConverter(obj);
            if (converter != null) {
                obj = converter.convert(obj, tip);
            }
            if (newArray == null) {
                newArray = Array.newInstance(obj.getClass(), Array.getLength(value));
            }
            try {
                Array.set(newArray, index, obj);
            } catch (IllegalArgumentException e) {
                throw new ConvertException(
                        MessageFormat.format("数组类型：{0}，插入失败元素：{1}", newArray.getClass(), obj), e);
            }

        }

        return newArray;
    }

    @Override
    public boolean supports(Object value) {
        return value != null && value.getClass().isArray();
    }
}
