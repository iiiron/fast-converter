package net.noboard.expression;

import net.noboard.fastconverter.AbstractConverterHandler;
import net.noboard.fastconverter.ConvertException;

public class Null2EnumFirstConstantConvertHandler extends AbstractConverterHandler<Object, Object, Class> {
    @Override
    protected Object doConvert(Object value, Class context) {
        if (value != null) {
            return value;
        }

        Class<Enum> clz = (Class<Enum>) context;
        return clz.getEnumConstants()[0].name();
    }

    @Override
    protected Class defaultContext() {
        return null;
    }
}
