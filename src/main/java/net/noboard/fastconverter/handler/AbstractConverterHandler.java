package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;

public abstract class AbstractConverterHandler<K, T, V> implements Converter<K, T, V> {

    protected abstract T doConvert(K value, V context);

    protected abstract V defaultContext();

    @Override
    public T convert(K value, V context) throws ConvertException {
        if (value == null) {
            return null;
        }
        return doConvert(value, context == null ? defaultContext() : context);
    }

    @Override
    public T convert(K value) throws ConvertException {
        return convert(value, defaultContext());
    }
}
