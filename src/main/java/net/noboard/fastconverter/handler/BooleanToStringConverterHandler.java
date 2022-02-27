package net.noboard.fastconverter.handler;

public class BooleanToStringConverterHandler extends AbstractConverterHandler<Boolean, String, Void> {
    @Override
    protected String doConvert(Boolean value, Void context) {
        return value.toString();
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
