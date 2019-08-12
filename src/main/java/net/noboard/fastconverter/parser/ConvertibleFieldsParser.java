package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.support.ConverterFactory;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashSet;

public class ConvertibleFieldsParser implements ConvertibleParser {
    @Override
    public ConvertibleMap parse(AnnotatedElement annotatedElement, String tip, String group) {
        LinkedHashSet<ConvertibleField> linkedHashSet = ConvertibleAnnotatedUtils.getMergedConvertField(annotatedElement, group);
        CMap first = null, last = null;
        for (ConvertibleField convertibleField : linkedHashSet) {
            if (first == null) {
                first = new CMap();
                last = first;
            } else {
                last.join(new CMap());
                last = (CMap) last.next();
            }

            if (convertibleField.converter() != Converter.class) {
                last.setConverter(ConverterFactory.get(convertibleField.converter()));
            }
            last.setTip(convertibleField.tip());
            last.setAbandon(convertibleField.abandon());
            last.setRetainNull(convertibleField.retainNull());
        }

        if (first == null) {
            first = new CMap();
        }

        return first;
    }
}