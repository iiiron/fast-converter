package net.noboard.fastconverter.handler.auto;

import net.noboard.fastconverter.AbstractConverterHandler;
import net.noboard.fastconverter.ConvertException;

public abstract class AbstractSensingConverter<T, K> extends AbstractConverterHandler<T, K> {

    protected abstract K converting(T value, Class<K> targetClass);

    protected abstract boolean supports(Class<?> sourceClass, Class<?> targetClass);

    @Override
    public K converting(T value, String tip) throws ConvertException {
        try {
            return converting(value, (Class<K>) Class.forName(tip));
        } catch (ClassNotFoundException e) {
            throw new ConvertException("error class for auto sensing convert");
        }
    }

    @Override
    public boolean supports(Object value, String tip) {
        if (value == null) {
            return false;
        }

        Class<?> target;
        try {
            target = Class.forName(tip);
        } catch (ClassNotFoundException e) {
            return false;
        }

        if (value.getClass().equals(target)) {
            return false;
        }

        return supports(value.getClass(), target);
    }
}
