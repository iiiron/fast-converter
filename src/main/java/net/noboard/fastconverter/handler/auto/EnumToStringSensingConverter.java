package net.noboard.fastconverter.handler.auto;

public class EnumToStringSensingConverter extends AbstractSensingConverter<Enum, String> {
    @Override
    protected String converting(Enum value, Class<String> targetClass) {
        return value.name();
    }

    @Override
    protected boolean supports(Class<?> sourceClass, Class<?> targetClass) {
        return Enum.class.isAssignableFrom(sourceClass) && String.class.isAssignableFrom(targetClass);
    }
}
