package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.Field;
import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.handler.support.FieldConverterHandler;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 用以将对象转换为map，丢弃值为null的数据
 *
 * @author wanxm
 * @return
 */
public class BeanToMapConverterHandler extends AbstractFilterBaseConverterHandler<Object, Map<String, Object>> {

    private static List<Converter> converters = new ArrayList<>();

    private final FieldConverterHandler fieldConverterHandler = new FieldConverterHandler();

    public BeanToMapConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected Map<String, Object> converting(Object value, String tip) throws ConvertException {
        Map<String, Object> map = new HashMap<>();

        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(value.getClass());
        } catch (IntrospectionException e) {
            throw new ConvertException("获取" + value.getClass() + "的bean info时出错。" + e.getMessage());
        }

        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            if ("class".equals(descriptor.getName())) {
                continue;
            }

            // 获取域值
            Object fieldValue;
            try {
                fieldValue = descriptor.getReadMethod().invoke(value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ConvertException(e);
            }

            // 对域名 及 是否隐藏的处理
            String mapKey;
            Field docField = null;
            try {
                docField = value.getClass().getDeclaredField(descriptor.getName()).getAnnotation(Field.class);
            } catch (NoSuchFieldException e) {
                throw new ConvertException(e);
            }
            if (docField != null) {
                if (docField.isHide()) {
                    continue;
                }

                mapKey = "".equals(docField.name())
                        ? descriptor.getName()
                        : docField.name();
            } else {
                mapKey = descriptor.getName();
            }

            // 转换域值
            Object mapValue = fieldValue;
            FieldConverter annotation = null;
            try {
                annotation = value.getClass().getDeclaredField(descriptor.getName()).getAnnotation(FieldConverter.class);
            } catch (NoSuchFieldException e) {
                throw new ConvertException(e);
            }
            if (annotation == null) {
                Converter converter = this.getConverter(fieldValue);
                if (converter != null) {
                    mapValue = converter.convert(fieldValue);
                }
            } else {
                if (fieldValue == null) {
                    continue;
                }
                mapValue = fieldConverterHandler.handler(annotation, fieldValue);
            }

            map.put(mapKey, mapValue);
        }

        return map;
    }

    @Override
    public boolean supports(Object value) {
        return value != null;
    }
}
