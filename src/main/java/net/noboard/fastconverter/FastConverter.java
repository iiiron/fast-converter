package net.noboard.fastconverter;

import net.noboard.fastconverter.filter.AbstractConverterFilter;
import net.noboard.fastconverter.handler.EnumToNameConverterHandler;
import net.noboard.fastconverter.handler.core.ArrayToArrayConverterHandler;
import net.noboard.fastconverter.handler.core.CollectionToCollectionConverterHandler;
import net.noboard.fastconverter.handler.core.CommonFilterBaseConverterHandler;
import net.noboard.fastconverter.handler.core.MapToMapConverterHandler;
import net.noboard.fastconverter.handler.core.bean.BeanMapping;
import net.noboard.fastconverter.handler.core.bean.ConvertibleBeanConverterHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FastConverter {

    private static final ConverterFilter defaultConverters;

    private static final Converter enterConverter;

    private static final List<Class<?>> parsedBean = new ArrayList<>();

    static {
        defaultConverters = new AbstractConverterFilter() {
            @Override
            protected void initConverters(List<Converter> converters) {
                converters.add(new EnumToNameConverterHandler());
                converters.add(new CollectionToCollectionConverterHandler<>(this));
                converters.add(new MapToMapConverterHandler<>(this));
                converters.add(new ArrayToArrayConverterHandler(this));
                converters.add(new ConvertibleBeanConverterHandler(this));
            }
        };
        enterConverter = new CommonFilterBaseConverterHandler<>(defaultConverters);
    }

    public static <T> T autoConvert(Object bean, String group) {
        if (bean == null) {
            return null;
        }
        return (T) doConvert(bean, group);
    }

    public static <T> T autoConvert(Object bean) {
        return autoConvert(bean, Converter.DEFAULT_GROUP);
    }

    public static <T> T autoConvert(Object source, Class<T> target, String group) {
        if (source == null) {
            return null;
        }

        try {
            BeanMapping.push(source.getClass(), target);
            return doConvert(source, group);
        } finally {
            BeanMapping.clear();
        }
    }

    public static <T> T autoConvert(Object source, Class<T> target) {
        return autoConvert(source, target, Converter.DEFAULT_GROUP);
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
