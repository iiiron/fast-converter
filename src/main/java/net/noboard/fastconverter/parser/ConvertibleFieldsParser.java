package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.CMap;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.support.ConverterCache;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashSet;

public class ConvertibleFieldsParser implements ConvertibleParser {

    private final ExpressionParser parser = new SpelExpressionParser();

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
            if (convertibleField.relevantClass() != Void.class) {
                last.setRelevantClass(convertibleField.relevantClass());
            }
            last.setAbandon(convertibleField.abandon());
            last.setIgnoreNull(convertibleField.ignoreNull());
            last.setAliasName(convertibleField.aliasName());
            if (!StringUtils.isEmpty(convertibleField.context())) {
                last.setConvertContext(parser.parseExpression(convertibleField.context()).getValue());
            }
        }

        if (first == null) {
            first = new CMap();
        }

        return first;
    }
}
