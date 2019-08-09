package net.noboard.fastconverter;

import com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime;
import net.noboard.fastconverter.handler.base.AbstractConverterHandler;
import net.noboard.fastconverter.handler.support.ConvertibleAnnotatedUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashSet;

public class ConvertibleFieldsParser implements ConvertibleParser {
    @Override
    public ConvertibleMap parse(AnnotatedElement annotatedElement, String tip, String group) {
        LinkedHashSet<ConvertibleField> linkedHashSet = ConvertibleAnnotatedUtils.getMergedConvertField(annotatedElement, group);
        CMap first = null, last = null;
        try {
            for (ConvertibleField convertibleField : linkedHashSet) {
                if (first == null) {
                    first = new CMap();
                    last = first;
                } else {
                    last.join(new CMap());
                    last = (CMap) last.next();
                }

                if (convertibleField.converter() != Converter.class) {
                    last.setConverter(convertibleField.converter().newInstance());
                }
                if (Converter.isTipHasMessage(convertibleField.tip())) {
                    last.setTip(convertibleField.tip());
                } else {
                    last.setTip(tip);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (first == null) {
            first = new CMap();
            first.setTip(tip);
        }

        return first;
    }
}
