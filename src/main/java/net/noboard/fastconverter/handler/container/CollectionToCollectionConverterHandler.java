package net.noboard.fastconverter.handler.container;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.AbstractConverterHandler;
import net.noboard.fastconverter.ConvertInfo;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Collection转换器
 * <p>
 * 该转换器将根据原Collection的实际类型生成一个新的相同类型的实例。并使用ConverterFilter中注册的转换器对
 * 原容器中的元素一一进行转换，转换结果推入新容器中。如果ConverterFilter没有筛选出转换器，则推入原数据。
 * <p>
 * 新容器生成失败时，将把新容器降级为ArrayList，并输出警告信息
 */
public class CollectionToCollectionConverterHandler<T, K>
        extends AbstractConverterHandler<Collection<T>, Collection<K>, ConvertInfo>
        implements ContainerConverter {
    @Override
    protected Collection<K> doConvert(Collection<T> value, ConvertInfo context) {
        Collection<K> collection;
        try {
            collection = value.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            collection = new ArrayList<>();
            System.err.println(String.format("class %s can't be implemented, the degradation measures implement the container with %s ",
                    value.getClass().getName(), collection.getClass()));
        }

        ParameterizedType sourceType = (ParameterizedType) context.getSourceType();
        ParameterizedType targetType = (ParameterizedType) context.getTargetType();

        ConvertInfo convertInfo = new ConvertInfo();
        convertInfo.setModeType(context.getModeType());
        convertInfo.setGroup(context.getGroup());
        convertInfo.setSourceType(sourceType.getActualTypeArguments()[0]);
        convertInfo.setTargetType(targetType.getActualTypeArguments()[0]);
        convertInfo.setConverterFilter(context.getConverterFilter());

        for (T obj : value) {
            collection.add((K) context.getConverterFilter().filter(obj, convertInfo).convert());
        }

        return collection;
    }

    @Override
    protected ConvertInfo defaultContext() {
        return null;
    }
}
