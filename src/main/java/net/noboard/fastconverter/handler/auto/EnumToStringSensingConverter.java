package net.noboard.fastconverter.handler.auto;

import net.noboard.fastconverter.ConvertException;

public class StringToEnumSensingConverter extends AbstractSensingConverter {

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        try {
            return Enum.valueOf((Class<Enum>) Class.forName(tip), (String) value);
        } catch (ClassNotFoundException e) {
            throw new ConvertException("can not find enum class " + tip);
        }
    }

    @Override
    protected boolean supports(Class<?> sourceClass, Class<?> targetClass) {
        return String.class.isAssignableFrom(sourceClass) && Enum.class.isAssignableFrom(targetClass);
    }
}
