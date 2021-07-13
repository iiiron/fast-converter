package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.AbstractConverterHandler;

import java.lang.reflect.ParameterizedType;

public class NameToEnumConverterHandler<T extends Enum<T>> extends AbstractConverterHandler<String, T> {
    @Override
    protected T converting(String s, String s2) throws ConvertException {
        return T.valueOf(getGenericClass(), s);
    }

    public Class<T> getGenericClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public boolean supports(Object o, String tip) {
        return true;
    }
}
