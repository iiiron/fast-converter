package net.noboard.fastconverter.handler.container;

import net.noboard.fastconverter.ContainerConverter;
import net.noboard.fastconverter.AbstractConverterHandler;
import net.noboard.fastconverter.ConvertInfo;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;


/**
 * Map转换器
 * <p>
 * 该转换器将根据原Map的实际类型生成一个新的相同类型的实例。并使用ConverterFilter中注册的转换器对
 * 原Map中的元素的value进行转换，将转换结果推入新的Map中（key不变，value为转换后的值）。如果转换器
 * 筛选器没有筛选出针对某一值的转换器，则将此原值推入新的Map中。
 * <p>
 * 新容器生成失败时，将把新容器降级为HashMap，并输出警告信息
 */
public class MapToMapConverterHandler<T, K, P, G> extends AbstractConverterHandler<Map<P, T>, Map<G, K>, ConvertInfo> implements ContainerConverter {
//    @Override
//    protected Map<Object, K> doConvert(Map<Object, T> value, ContainerConverterContext context) {
//        Map<Object, K> newMap;
//        try {
//            newMap = value.getClass().newInstance();
//        } catch (InstantiationException | IllegalAccessException e) {
//            newMap = new HashMap<>();
//            System.err.println(String.format("class %s can't be implemented, the degradation measures implement the container with %s ",
//                    value.getClass(), newMap.getClass()));
//        }
//
//        for (Map.Entry entry : value.entrySet()) {
//            newMap.put(entry.getKey(), (K) context.getConverterFilter().filter(entry.getValue(), context.getConvertInfo()));
//        }
//
//        return newMap;
//    }

    @Override
    protected Map<G, K> doConvert(Map<P, T> value, ConvertInfo context) {
        Map<G, K> newMap;
        try {
            // todo wanxm 需要做类型识别, 要看这个type是ParameterizedType还是Class, 如果是前者, 要从他
            Class rawType = (Class) ((ParameterizedType) context.getTargetType()).getRawType();
            newMap = (Map<G, K>) rawType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            newMap = new HashMap<>();
            System.err.println(String.format("class %s can't be implemented, the degradation measures implement the container with %s ",
                    value.getClass(), newMap.getClass()));
        }

        for (Map.Entry entry : value.entrySet()) {
            ParameterizedType sourceType = (ParameterizedType) context.getSourceType();
            ParameterizedType targetType = (ParameterizedType) context.getTargetType();

            ConvertInfo keyConvertInfo = new ConvertInfo();
            keyConvertInfo.setModeType(context.getModeType());
            keyConvertInfo.setGroup(context.getGroup());
            keyConvertInfo.setSourceType(sourceType.getActualTypeArguments()[0]);
            keyConvertInfo.setTargetType(targetType.getActualTypeArguments()[0]);
            keyConvertInfo.setConverterFilter(context.getConverterFilter());

            ConvertInfo valueConvertInfo = new ConvertInfo();
            valueConvertInfo.setModeType(context.getModeType());
            valueConvertInfo.setGroup(context.getGroup());
            valueConvertInfo.setSourceType(sourceType.getActualTypeArguments()[1]);
            valueConvertInfo.setTargetType(targetType.getActualTypeArguments()[1]);
            valueConvertInfo.setConverterFilter(context.getConverterFilter());

            newMap.put((G) context.getConverterFilter().filter(entry.getKey(), keyConvertInfo).convert(),
                    (K) context.getConverterFilter().filter(entry.getValue(), valueConvertInfo).convert());
        }
        return newMap;
    }

    @Override
    protected ConvertInfo defaultContext() {
        return null;
    }
}
