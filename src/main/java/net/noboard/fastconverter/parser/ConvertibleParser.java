package net.noboard.fastconverter.parser;

import java.lang.reflect.AnnotatedElement;

public interface ConvertibleParser {
    /**
     * @param annotatedElement 该次解析的 AnnotatedElement
     * @param group            该次解析的指定分组(即有效分组)
     * @return
     */
    ConvertibleMap parse(AnnotatedElement annotatedElement, String group);
}
