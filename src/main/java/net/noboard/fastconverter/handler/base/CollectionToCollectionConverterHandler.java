package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.handler.base.AbstractFilterBaseConverterHandler;

import java.util.Collection;

/**
 * Collection<T> -> Collection<K>
 *
 * @author wanxm
 */
public class CollectionToCollectionConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<Collection<T>, Collection<K>> {

    public CollectionToCollectionConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected Collection<K> converting(Collection<T> value, String tip) throws ConvertException {
        Collection<K> list = null;
        try {
            list = value.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ConvertException("实例化" + value.getClass() + "失败");

        }

        for (T obj : value) {
            Converter converter = this.getConverter(obj);
            if (converter == null) {
                throw new ConvertException("没有转换器可以处理" + obj);
            } else{
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
