package net.noboard.fastconverter;

import net.noboard.fastconverter.filter.AbstractConverterFilter;
import net.noboard.fastconverter.handler.core.ArrayToArrayConverterHandler;
import net.noboard.fastconverter.handler.core.CollectionToCollectionConverterHandler;
import net.noboard.fastconverter.handler.core.MapToMapConverterHandler;
import net.noboard.fastconverter.handler.core.bean.*;
import net.noboard.fastconverter.handler.core.CommonFilterBaseConverterHandler;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;
import net.noboard.fastconverter.support.ConvertibleBeanCache;
import net.noboard.fastconverter.support.TypeProbeUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FastConverter {

    private static final ConverterFilter defaultConverters;

    private static final Converter enterConverter;

    private static final List<Class<?>> parsedBean = new ArrayList<>();

    static {
        defaultConverters = new AbstractConverterFilter() {
            @Override
            protected void initConverters(List<Converter> converters) {
                converters.add(new CollectionToCollectionConverterHandler<>(this));
                converters.add(new MapToMapConverterHandler<>(this));
                converters.add(new ArrayToArrayConverterHandler(this));
                converters.add(new ConvertibleBeanConverterHandler(this));
            }
        };
        enterConverter = new CommonFilterBaseConverterHandler<>(defaultConverters);
    }

//    public static <T> T autoConvert(Object bean, String group) {
//        if (bean == null) {
//            return null;
//        }
//        parseBean(bean.getClass());
//        return (T) doConvert(bean, group);
//    }

//    public static <T> T autoConvert(Object bean) {
//        return autoConvert(bean, Converter.DEFAULT_GROUP);
//    }

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

    private static void parseBean(Class<?> clazz) {
        if (parsedBean.contains(clazz)) {
            return;
        }
        parsedBean.add(clazz);

        Set<ConvertibleBean> set = ConvertibleBeanCache.get(clazz);
        for (ConvertibleBean o : set) {
            try {
                TargetBeanMapping.push(o.group(), clazz, o.targetClass().equals(Void.class) ? Class.forName(o.targetName()) : o.targetClass());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor anchorPD : beanInfo.getPropertyDescriptors()) {
                try {
                    Field field = clazz.getDeclaredField(anchorPD.getName());
                    Class<?> fieldType = TypeProbeUtil.find(field);
                    parseBean(fieldType);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }
}
