package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.support.GroupUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.reflect.AnnotatedElement;

public class ConvertibleFieldParser implements ConvertibleParser {
    @Override
    public ConvertibleMap parse(AnnotatedElement annotatedElement, String tip, String group) {
        CMap cMap = new CMap();
        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(annotatedElement, ConvertibleField.class);
        Class<? extends Converter> converterClass = (Class<? extends Converter>) annotationAttributes.get("converter");
        if (GroupUtils.checkGroup(annotationAttributes.getString("group"), group)) {
            if (converterClass != Converter.class) {
                try {
                    cMap.setConverter(converterClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new IllegalArgumentException(String.format("the class %s pointed by attribute 'converter' in @ConvertibleField can not be implemented", converterClass.getName()));
                }
            }
            cMap.setTip(annotationAttributes.getString("tip"));
            cMap.setAbandon(annotationAttributes.getBoolean("abandon"));
            cMap.setRetainNull(annotationAttributes.getBoolean("retainNull"));
        }

        return cMap;
    }
}
