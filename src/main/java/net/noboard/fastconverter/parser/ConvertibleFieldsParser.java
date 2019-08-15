package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.CMap;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.support.ConverterCache;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashSet;

public class ConvertibleFieldsParser implements ConvertibleParser {
    @Override
    public ConvertibleMap parse(AnnotatedElement annotatedElement, String group) {
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
                last.setConverter(ConverterCache.get(convertibleField.converter()));
            }
            last.setTip(convertibleField.tip());
            last.setAbandon(convertibleField.abandon());
            last.setRetainNull(convertibleField.retainNull());
            last.setNameTo(convertibleField.nameTo());
        }

        if (first == null) {
            first = new CMap();
        }

        return first;
    }
}
