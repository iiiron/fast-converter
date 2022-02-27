package net.noboard.fastconverter.handler;

public class EnumToNameConverterHandler extends AbstractConverterHandler<Enum<?>, String, Void> {
    @Override
    protected String doConvert(Enum<?> value, Void context) {
        return value.name();
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
