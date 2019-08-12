package net.noboard.fastconverter.support;

import net.noboard.fastconverter.Converter;

import java.util.HashMap;
import java.util.Map;

public class ConverterFactory {

    private static Map<Class, Converter> converterMap = new HashMap<>();

    public static Converter get(Class<? extends Converter> clazz) {
        if (converterMap.containsKey(clazz)) {
            return converterMap.get(clazz);
        }

        try {
            Converter converter = clazz.newInstance();
            converterMap.put(clazz, converter);
            return converter;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("the point class %s of Converter can't be implemented", clazz.getName()), e);
        }
    }
}
