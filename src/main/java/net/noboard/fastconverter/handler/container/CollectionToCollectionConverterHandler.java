package net.noboard.fastconverter.handler.container;

import net.noboard.fastconverter.*;

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
public class CollectionToCollectionConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<Collection<T>, Collection<K>> implements ContainerConverter {

    public CollectionToCollectionConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected Collection<K> converting(Collection<T> value, String tip) throws ConvertException {
        Collection<K> collection;
        try {
            collection = value.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            collection = new ArrayList<>();
            System.err.println(String.format("class %s can't be implemented, the degradation measures implement the container with %s ",
                    value.getClass().getName(), collection.getClass()));
        }

        Converter converter;
        Object newV;
        for (T obj : value) {
            converter = this.filter(obj);
            if (converter == null) {
                newV = obj;
            } else if (Converter.isTipHasMessage(tip)) {
                newV = converter.convert(obj, tip);
            } else {
                newV = converter.convert(obj);
            }
            collection.add((K) newV);
        }


        return collection;
    }

    @Override
    public boolean supports(Object value, String tip) {
        return value != null && Collection.class.isAssignableFrom(value.getClass());
    }
}
