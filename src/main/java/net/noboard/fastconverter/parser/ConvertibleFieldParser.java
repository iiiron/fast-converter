package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.support.ConverterFactory;
import net.noboard.fastconverter.support.GroupUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.reflect.AnnotatedElement;

public class ConvertibleFieldParser implements ConvertibleParser {

    @Override
    public ConvertibleMap parse(AnnotatedElement annotatedElement, String tip, String group) {
        CMap cMap = new CMap();
        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(annotatedElement, ConvertibleField.class);
        Class converterClass = (Class) annotationAttributes.get("converter");
        if (GroupUtils.checkGroup(annotationAttributes.getString("group"), group)) {
            if (converterClass != Converter.class) {
                cMap.setConverter(ConverterFactory.get(converterClass));
            }
            cMap.setTip(annotationAttributes.getString("tip"));
            cMap.setAbandon(annotationAttributes.getBoolean("abandon"));
            cMap.setRetainNull(annotationAttributes.getBoolean("retainNull"));
        }

        return cMap;
    }
}