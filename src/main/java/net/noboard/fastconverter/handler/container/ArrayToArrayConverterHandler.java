package net.noboard.fastconverter.handler.container;

import net.noboard.fastconverter.ContainerConverter;
import net.noboard.fastconverter.AbstractConverterHandler;
import net.noboard.fastconverter.ConvertInfo;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;

/**
 * 数组到数组转换器
 * <p>
 * 该转换器会产生一个可以放置转换后数据的新数组，并使用ConverterFilter中注册的转换器对
 * 原数组中的元素一一进行转换，转换结果推入新数组中。如果过滤器没有找到可用的转换器则直接
 * 将原数据推入新数组中
 * <p>
 * 注意，由于java存在自动装箱，int[]经过转换后将变为Integer[]，其他基本数据类型同理。
 */
public class ArrayToArrayConverterHandler extends AbstractConverterHandler<Object, Object, ConvertInfo> implements ContainerConverter {
    @Override
    protected Object doConvert(Object value, ConvertInfo context) {
        Object newArray = null;

        ParameterizedType sourceType = (ParameterizedType) context.getSourceType();
        ParameterizedType targetType = (ParameterizedType) context.getTargetType();

        ConvertInfo convertInfo = new ConvertInfo();
        convertInfo.setModeType(context.getModeType());
        convertInfo.setGroup(context.getGroup());
        convertInfo.setSourceType(sourceType.getActualTypeArguments()[0]);
        convertInfo.setTargetType(targetType.getActualTypeArguments()[0]);

        for (int index = 0; index < Array.getLength(value); index++) {
            Object result = context.getConverterFilter().filter(Array.get(value, index), convertInfo).convert();
            if (newArray == null && result != null) {
                newArray = Array.newInstance(result.getClass(), Array.getLength(value));
            }
            if (newArray != null) {
                Array.set(newArray, index, result);
            }
        }

        return newArray;
    }

    @Override
    protected ConvertInfo defaultContext() {
        return null;
    }
}
