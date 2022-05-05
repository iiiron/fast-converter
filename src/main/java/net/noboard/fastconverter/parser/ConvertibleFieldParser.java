package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.CMap;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.support.ConverterCache;
import net.noboard.fastconverter.support.GroupUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.lang.reflect.AnnotatedElement;

public class ConvertibleFieldParser implements ConvertibleParser {

    private final ExpressionParser parser = new SpelExpressionParser();

    @Override
    public ConvertibleMap parse(AnnotatedElement annotatedElement, String group) {
        ConvertibleField annotationAttributes = AnnotatedElementUtils.getMergedAnnotation(annotatedElement, ConvertibleField.class);

        CMap cMap = new CMap();
        if (annotationAttributes != null && GroupUtils.checkGroup(annotationAttributes.group(), group)) {
            if (annotationAttributes.converter() != Converter.class) {
                cMap.setConverter(ConverterCache.get(annotationAttributes.converter()));
            }
            cMap.setAbandon(annotationAttributes.abandon());
            cMap.setIgnoreNull(annotationAttributes.ignoreNull());
            cMap.setAliasName(annotationAttributes.aliasName());
            if (!StringUtils.isEmpty(annotationAttributes.context())) {
                cMap.setConvertContext(parser.parseExpression(annotationAttributes.context()).getValue());
            }
        }

        return cMap;
    }
}
