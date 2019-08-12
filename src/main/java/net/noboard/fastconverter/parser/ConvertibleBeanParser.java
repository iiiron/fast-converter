package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.core.bean.ConvertibleBeanConverterHandler;
import net.noboard.fastconverter.support.ConverterFactory;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.lang.reflect.AnnotatedElement;

public class ConvertibleBeanParser implements ConvertibleParser {

    @Override
    public ConvertibleMap parse(AnnotatedElement annotatedElement, String tip, String group) {
        ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(annotatedElement, group);

        CMap cMap = new CMap();
        if (convertibleBean.converter() != Converter.class) {
            cMap.setConverter(ConverterFactory.get(convertibleBean.converter()));
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
