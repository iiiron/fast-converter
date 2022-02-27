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
        ConvertibleField annotationAttributes = AnnotatedElementUtils.getMergedAnnotation(annotatedElement, ConvertibleField.class);

        CMap cMap = new CMap();
        if (annotationAttributes != null && GroupUtils.checkGroup(annotationAttributes.group(), group)) {
            if (annotationAttributes.converter() != Converter.class) {
                cMap.setConverter(ConverterCache.get(annotationAttributes.converter()));
            }
            if (annotationAttributes.relevantClass() != Void.class) {
                cMap.setRelevantClass(annotationAttributes.relevantClass());
            }
            cMap.setTip(annotationAttributes.tip());
            cMap.setAbandon(annotationAttributes.abandon());
            cMap.setRetainNull(annotationAttributes.retainNull());
            cMap.setAliasName(annotationAttributes.aliasName());
        }

        return cMap;
    }
}
