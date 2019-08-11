package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.core.bean.ConvertibleBeanConverterHandler;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.lang.reflect.AnnotatedElement;

public class ConvertibleBeanParser implements ConvertibleParser {

    // TODO BeanToMap的支持
    @Override
    public ConvertibleMap parse(AnnotatedElement annotatedElement, String tip, String group) {
        ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(annotatedElement, group);

        CMap cMap = new CMap();
        if (convertibleBean.converter() != Converter.class) {
            try {
                cMap.setConverter(convertibleBean.converter().newInstance());
            } catch (IllegalAccessException | InstantiationException e) {
                throw new IllegalArgumentException(String.format("the class %s pointed by attribute 'converter' in @ConvertibleBean can not be implemented", convertibleBean.converter().getName()));
            }
        } else {
            ConvertibleBeanConverterHandler convertibleBeanConverterHandler =
                    new ConvertibleBeanConverterHandler(new CommonConverterFilter(), convertibleBean.group());
            if (convertibleBean.nested()) {
                convertibleBeanConverterHandler.getFilter().addLast(convertibleBeanConverterHandler);
            }
            cMap.setConverter(convertibleBeanConverterHandler);
        }
        cMap.setTip(convertibleBean.tip());
        return cMap;
    }
}
