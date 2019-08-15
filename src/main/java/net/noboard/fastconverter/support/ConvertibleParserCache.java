package net.noboard.fastconverter.support;

import net.noboard.fastconverter.parser.ConvertibleParser;

import java.util.HashMap;
import java.util.Map;

public class ConvertibleParserCache {

    private static Map<Class, ConvertibleParser> map = new HashMap<>();

    public static ConvertibleParser get(Class<? extends ConvertibleParser> clazz) {
        if (map.containsKey(clazz)) {
            return map.get(clazz);
        }
        try {
            ConvertibleParser parser = clazz.newInstance();
            map.put(clazz, parser);
            return parser;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("the point class %s of ConvertibleParser can't be implemented", clazz.getName()), e);
        }
    }
}
