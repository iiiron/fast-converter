package net.noboard.fastconverter.handler;

public class SkipConverterHandler extends AbstractConverterHandler<Object, Object, Void>{
    @Override
    protected Object doConvert(Object value, Void context) {
        return value;
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
