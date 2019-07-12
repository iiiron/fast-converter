package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

import java.util.Collection;

/**
 * Collection转换器
 * <p>
 * 该转换器将根据原Collection的实际类型生成一个新的相同类型的实例。并使用ConverterFilter中注册的转换器对
 * 原容器中的元素一一进行转换，转换结果推入新容器中。如果ConverterFilter没有筛选出转换器，则推入原数据。
 */
public class CollectionToCollectionConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<Collection<T>, Collection<K>> {

    public CollectionToCollectionConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected Collection<K> converting(Collection<T> value, String tip) throws ConvertException {
        Collection<K> list;
        try {
            list = value.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ConvertException("实例化" + value.getClass() + "失败", e);

        }

        for (T obj : value) {
            Converter converter = this.getConverter(obj);
            if (converter == null) {
                list.add((K) obj);
            } else {
                list.add((K) converter.convert(obj, tip));
            }
        }

        return list;
    }

    @Override
    public boolean supports(Object value) {
        return value != null && Collection.class.isAssignableFrom(value.getClass());
    }
}
