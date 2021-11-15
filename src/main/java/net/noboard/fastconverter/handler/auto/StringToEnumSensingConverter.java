package net.noboard.fastconverter.handler.auto;

public class StringToEnumSensingConverter extends AbstractSensingConverter<String, Enum> {

    @Override
    protected Enum converting(String value, Class<Enum> targetClass) {
        return Enum.valueOf(targetClass, value);
    }

    @Override
    protected boolean supports(Class<?> sourceClass, Class<?> targetClass) {
        return String.class.isAssignableFrom(sourceClass) && Enum.class.isAssignableFrom(targetClass);
    }
}
