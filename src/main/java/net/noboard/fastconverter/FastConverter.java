package net.noboard.fastconverter;

import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.core.bean.*;
import net.noboard.fastconverter.handler.core.CommonFilterBaseConverterHandler;

import java.util.List;

public class FastConverter {

    private static BeanConverterFilter beanConverters;

    private static ConverterFilter defaultConverters;

    private static Converter enterConverter;

    static {
        defaultConverters = new CommonConverterFilter();

        enterConverter = new CommonFilterBaseConverterHandler(defaultConverters);

        beanConverters = new AbstractBeanConverterFilter(defaultConverters) {
            @Override
            protected void initConverters(List<BeanConverter> converters) {
                converters.add(new BeanToBeanConverter(defaultConverters));
            }
        };

        ConvertibleBeanConverterHandler converter = new ConvertibleBeanConverterHandler(defaultConverters);
        converter.setBeanConverterFilter(beanConverters);
        defaultConverters.addLast(converter);
    }

    public static Object autoConvert(Object bean, String group) {
        if (bean == null) {
            return null;
        }
        return enterConverter.convert(bean, group);
    }

    public static Object autoConvert(Object bean) {
        return autoConvert(bean, Convertible.defaultGroup);
    }

    public BeanConverterFilter customBeanConverters() {
        return beanConverters;
    }

    public ConverterFilter customDefaultConverters() {
        return defaultConverters;
    }
}
