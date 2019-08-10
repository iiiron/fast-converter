package net.noboard.fastconverter.handler.core;

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
        Converter converter = null;
        Object newArray = null, newV = null, oldV = null;
        try {
            for (int index = 0; index < Array.getLength(value); index++) {
                oldV = Array.get(value, index);
                newV = oldV;
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
        } catch (Exception e) {
            throw new ConvertException(
                    MessageFormat.format(
                            "旧容器类型：{0}，旧元素类型：{1}，新容器类型：{2}，新元素类型：{3}，转换器类型：{4}，",
                            value.getClass().getName(),
                            oldV == null ? "null" : oldV.getClass().getName(),
                            newArray == null ? "null" : newArray.getClass().getName(),
                            newV == null ? "null" : newV.getClass().getName(),
                            converter == null ? "null" : converter.getClass().getName()),
                    e);
        }


        return newArray;
    }

    @Override
    public boolean supports(Object value) {
        return value != null && value.getClass().isArray();
    }
}
