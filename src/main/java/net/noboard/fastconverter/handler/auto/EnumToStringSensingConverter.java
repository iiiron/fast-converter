package net.noboard.fastconverter.handler.auto;

import net.noboard.fastconverter.ConvertException;

public class EnumToStringSensingConverter extends AbstractSensingConverter {

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        return ((Enum) value).name();
    }

    @Override
    protected boolean supports(Class<?> sourceClass, Class<?> targetClass) {
        return Enum.class.isAssignableFrom(sourceClass) && String.class.isAssignableFrom(targetClass);
    }
}
