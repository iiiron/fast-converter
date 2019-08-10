package net.noboard.fastconverter;

import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.core.ConvertibleBeanConverterHandler;
import net.noboard.fastconverter.handler.core.CommonFilterBaseConverterHandler;

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
        commonFilterBaseConverterHandler.getFilter().addLast(new ConvertibleBeanConverterHandler(commonFilterBaseConverterHandler.getFilter()));
        return commonFilterBaseConverterHandler;
    }
}
