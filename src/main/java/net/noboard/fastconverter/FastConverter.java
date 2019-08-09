package net.noboard.fastconverter;

import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.base.BeanToBeanConverterHandler;
import net.noboard.fastconverter.handler.base.CommonFilterBaseConverterHandler;
import net.noboard.fastconverter.handler.support.ConvertibleAnnotatedUtils;

public class FastConverter {
    public static Object autoConvert(Object bean, String group) {
        if (bean == null) {
            return null;
        }
        return getConverter().convert(bean, group);
    }

    public static Object autoConvert(Object bean) {
        return autoConvert(bean, Convertible.defaultGroup);
    }

    public static Converter getConverter() {
        CommonFilterBaseConverterHandler commonFilterBaseConverterHandler = new CommonFilterBaseConverterHandler(new CommonConverterFilter());
        commonFilterBaseConverterHandler.getFilter().addLast(new BeanToBeanConverterHandler(commonFilterBaseConverterHandler.getFilter()));
        return commonFilterBaseConverterHandler;
    }
}
