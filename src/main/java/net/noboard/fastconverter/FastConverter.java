package net.noboard.fastconverter;

import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.core.bean.*;
import net.noboard.fastconverter.handler.core.CommonFilterBaseConverterHandler;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.util.List;
import java.util.Objects;

public class FastConverter {

    private static final ConverterFilter defaultConverters;

    private static final Converter enterConverter;

    static {
        defaultConverters = new CommonConverterFilter();
        defaultConverters.addLast(new ConvertibleBeanConverterHandler(defaultConverters));
        enterConverter = new CommonFilterBaseConverterHandler<>(defaultConverters);
    }

    public static <T> T autoConvert(Object bean, String group) {
        if (bean == null) {
            return null;
        }
        ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(bean.getClass(), group);
        if (convertibleBean != null && convertibleBean.type() == ConvertibleBeanType.SOURCE) {
            BeanMapping.push(bean.getClass(), ConvertibleAnnotatedUtils.getTargetClass(convertibleBean));
        }
        try {
            return (T) doConvert(bean, group);
        } finally {
            BeanMapping.clear();
        }

    }

    public static Object autoConvert(Object bean) {
        return autoConvert(bean, Converter.DEFAULT_GROUP);
    }

    public static Object autoConvert(Object source, Class<?> target, String group) {
        if (source == null) {
            return null;
        }
        BeanMapping.push(source.getClass(), target);
        try {
            return autoConvert(source, group);
        } finally {
            BeanMapping.clear();
        }
    }

    private static <T> T doConvert(Object bean, String group) {
        Objects.requireNonNull(bean);
        return (T) enterConverter.convert(bean, group);
    }

    // todo wanxm
    public static ConverterFilter customDefaultConverters() {
        return defaultConverters;
    }
}
