package net.noboard.fastconverter;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

public class ConvertibleFieldParser extends AbstractConvertibleParser {
    @Override
    public ConvertibleMap parse() {
        CMap cMap = new CMap();
        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(super.annotatedElement, ConvertibleField.class);
        Class<? extends Converter> converterClass = (Class<? extends Converter>) annotationAttributes.get("converter");
        if (annotationAttributes.getString("group").equals(super.group)) {
            try {
                if (converterClass != Converter.class) {
                    Converter converter = converterClass.newInstance();
                    cMap.setConverter(converter);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException(String.format("the class %s pointed by attribute 'converter' in @ConvertibleField can not be implemented", converterClass.getName()));
            }
        }
        cMap.setGroup(annotationAttributes.getString("group"));
        cMap.setTip(annotationAttributes.getString("tip"));
        return cMap;
    }
}
