package net.noboard.fastconverter.handler.auto;

import net.noboard.fastconverter.AbstractConverterHandler;

public abstract class AbstractSensingConverter extends AbstractConverterHandler<Object, Object> {

    protected abstract boolean supports(Class<?> sourceClass, Class<?> targetClass);

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
