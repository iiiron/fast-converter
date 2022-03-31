package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;
import net.noboard.fastconverter.ConvertException;
import org.springframework.util.StringUtils;

public class NameToEnumConverterHandler<T extends Enum<T>> extends AbstractConverterHandler<String, T, Class<T>> {
    @Override
    protected T doConvert(String value, Class<T> context) {
        try {
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            return Enum.valueOf(context, value);
        } catch (Exception e) {
            throw new ConvertException("No enum constant '" + value + "'" + " in " + context, e);
        }
    }

    @Override
    protected Class<T> defaultContext() {
        return null;
    }
}
