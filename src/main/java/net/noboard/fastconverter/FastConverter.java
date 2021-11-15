package net.noboard.fastconverter;

import net.noboard.fastconverter.handler.container.ArrayToArrayConverterHandler;
import net.noboard.fastconverter.handler.container.CollectionToCollectionConverterHandler;
import net.noboard.fastconverter.handler.CommonFilterBaseConverterHandler;
import net.noboard.fastconverter.handler.container.MapToMapConverterHandler;
import net.noboard.fastconverter.handler.bean.BeanMapping;
import net.noboard.fastconverter.handler.bean.ConvertibleBeanConverterHandler;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.util.List;
import java.util.Objects;

public class FastConverter {

    private static final ConverterFilter defaultConverters;

    private static final Converter enterConverter;

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
            ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(target, group);
            if (convertibleBean != null && ConvertibleBeanType.TARGET.equals(convertibleBean.type())) {
                BeanMapping.push(ConvertibleAnnotatedUtils.getTargetClass(convertibleBean), target);
            } else {
                throw new ConvertException(String.format("the target bean %s defect @ConvertibleBean with type of ConvertibleBeanType.TARGET", target));
            }
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
