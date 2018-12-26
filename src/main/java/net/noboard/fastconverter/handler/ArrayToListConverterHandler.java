package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * [] -> List[K]
 *
 * @author wanxm
 */
public class ArrayToListConverterHandler<K> extends AbstractFilterBaseConverterHandler<Object, List<K>> {

    public ArrayToListConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected List<K> converting(Object value, String tip) throws ConvertException {
        ArrayList<K> list = new ArrayList<>();

        for (int i = 0; i < Array.getLength(value); i++) {
            Object t = Array.get(value, i);
            Converter converter = this.getConverter(t);
            if (converter == null) {
                throw new ConvertException("没有转换器能够处理数据" + t);
            } else {
                list.add((K) converter.convert(t, tip));
            }
        }
        return list;
    }

    @Override
    public boolean supports(Object value) {
        return value != null && value.getClass().isArray();
    }
}
