package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Collection<T> -> List<K>
 *
 * @author wanxm
 */
public class CollectionToListConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<Collection<T>, List<K>> {

    public CollectionToListConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected List<K> converting(Collection<T> value, String tip) throws ConvertException {
        ArrayList<K> list = new ArrayList<>();

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
        return value != null && value.getClass().isAssignableFrom(Collection.class);
    }
}
