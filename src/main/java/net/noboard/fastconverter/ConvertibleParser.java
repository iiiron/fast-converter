package net.noboard.fastconverter;

import java.lang.reflect.AnnotatedElement;

public interface ConvertibleParser {
    /**
     *
     * @param annotatedElement 来自被注释 AnnotatedElement
     */
    void setAnnotatedElement(AnnotatedElement annotatedElement);

    void setGroup(String group);

    void setTip(String tip);

    ConvertibleMap parse();
}
