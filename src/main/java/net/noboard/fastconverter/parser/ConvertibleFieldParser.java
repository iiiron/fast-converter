package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.CMap;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.support.ConverterCache;
import net.noboard.fastconverter.support.GroupUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.reflect.AnnotatedElement;

public class ConvertibleFieldParser implements ConvertibleParser {

    @Override
    public ConvertibleMap parse(AnnotatedElement annotatedElement, String group) {
        CMap cMap = new CMap();
        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(annotatedElement, ConvertibleField.class);
        Class converterClass = (Class) annotationAttributes.get("converter");
        if (GroupUtils.checkGroup(annotationAttributes.getString("group"), group)) {
            if (converterClass != Converter.class) {
                cMap.setConverter(ConverterCache.get(converterClass));
            }
            cMap.setTip(annotationAttributes.getString("tip"));
            cMap.setAbandon(annotationAttributes.getBoolean("abandon"));
            cMap.setRetainNull(annotationAttributes.getBoolean("retainNull"));
            cMap.setNameTo(annotationAttributes.getString("nameTo"));
        }

        return cMap;
    }
}
