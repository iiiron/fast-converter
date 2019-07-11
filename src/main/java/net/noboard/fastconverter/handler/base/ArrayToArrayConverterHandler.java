package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayToArrayConverterHandler extends AbstractFilterBaseConverterHandler<Object, Object> {

    public ArrayToArrayConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        Converter converter = null;
        Object newArray = null;
        for (int index = 0; index < Array.getLength(value); index++) {
            Object obj = Array.get(value, index);
            if (converter == null) {
                converter = this.getConverter(obj);
                if (converter == null) {
                    throw new ConvertException("没有转换器能够处理数据" + obj);
                } else {
                    Object result = converter.convert(obj, tip);
                    newArray = Array.newInstance(result.getClass(), Array.getLength(value));
                    Array.set(newArray, index, result);
                }
            } else {
                Array.set(newArray, index, converter.convert(obj, tip));
            }
        }
        return newArray;
    }

    @Override
    public boolean supports(Object value) {
        return value != null && value.getClass().isArray();
    }
}
