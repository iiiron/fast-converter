package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.handler.base.AbstractFilterBaseConverterHandler;

import java.lang.reflect.Array;

/**
 * [] -> List[K]
 *
 * @author wanxm
 */
public class ArrayToArrayConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<T[], K[]> {

    public ArrayToArrayConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected K[] converting(T[] value, String tip) throws ConvertException {
        int index = 0;
        Converter converter = null;
        Object newArray = null;
        for (T obj : value) {
            if (converter == null) {
                converter = this.getConverter(obj);
                if (converter == null) {
                    throw new ConvertException("没有转换器能够处理数据" + obj);
                } else {
                    Object result = converter.convert(obj, tip);
                    newArray = Array.newInstance(result.getClass(), value.length);
                    Array.set(newArray, index++, result);
                }
            } else {
                Array.set(newArray, index++, converter.convert(obj, tip));
            }
        }
        return (K[]) newArray;
    }

    @Override
    public boolean supports(Object value) {
        return value != null && value.getClass().isArray();
    }
}
