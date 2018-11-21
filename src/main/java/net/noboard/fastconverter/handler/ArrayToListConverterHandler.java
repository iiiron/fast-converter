package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * T[] -> List[K]
 *
 * @author wanxm
 */
public class ArrayToListConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<T[], List<K>> {

    public ArrayToListConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    public boolean supports(T[] value) {
        return value != null;
    }

    @Override
    protected List<K> converting(T[] value, String tip) throws ConvertException {
        ArrayList<K> list = new ArrayList<>();

        for (T t : value) {
            Converter converter = this.getConverter(t);
            if (converter == null) {
                throw new ConvertException("没有转换器能够处理数据" + t);
            } else {
                list.add((K) converter.convert(t, tip));
            }
        }

        return list;
    }
}
