package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.ConvertibleMap;

import java.lang.reflect.AnnotatedElement;

public interface ConvertibleParser {
    /**
     * @param annotatedElement 该次解析的 AnnotatedElement
     * @param tip              该次解析的指定分组(即有效分组)
     * @param group            该次解析的提示
     * @return
     */
    ConvertibleMap parse(AnnotatedElement annotatedElement, String tip, String group);
}
