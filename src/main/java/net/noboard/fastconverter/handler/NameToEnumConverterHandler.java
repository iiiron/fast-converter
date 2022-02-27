package net.noboard.fastconverter.handler;

// todo 有瑕疵
public class NameToEnumConverterHandler<T extends Enum<T>> extends AbstractConverterHandler<String, T, Class<T>> {
    @Override
    protected T doConvert(String value, Class<T> context) {
        return Enum.valueOf(context, value);
    }

    @Override
    protected Class<T> defaultContext() {
        return null;
    }
}
