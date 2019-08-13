package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.core.bean.ConvertibleBeanConverterHandler;
import net.noboard.fastconverter.support.ConverterCache;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.lang.reflect.AnnotatedElement;

public class ConvertibleBeanParser implements ConvertibleParser {

    @Override
    public ConvertibleMap parse(AnnotatedElement annotatedElement, String tip, String group) {
        ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(annotatedElement, group);

        CMap cMap = new CMap();
        cMap.setTip(convertibleBean.group());
        return cMap;
    }
}
