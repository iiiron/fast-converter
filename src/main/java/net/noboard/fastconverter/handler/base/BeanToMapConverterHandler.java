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
import java.text.MessageFormat;
import java.util.*;

/**
 * 用以将对象转换为map。
 * <p>
 * 通过@FieldConverter指定了转换器的域为null，则对应key不会出现在map中。
 * 没有转换器支持的域将以原值推入map中。
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
            throw new ConvertException("获取" + value.getClass() + "的bean info时出错。", e);
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
            Field docField;
            try {
                docField = value.getClass().getDeclaredField(descriptor.getName()).getAnnotation(Field.class);
            } catch (NoSuchFieldException e) {
                throw new ConvertException(
                        MessageFormat.format("BeanToMapConverterHandler反射获取对象域时发生异常：NoSuchFieldException  {0}.{1}  通常是因为在转换器过滤器中没有添加对相关类的支持，导致某些非Bean的java类被错误的抛给了BeanToMapConverterHandler去处理", value.getClass().getName(), descriptor.getName()),
                        e);
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
            FieldConverter[] annotations;
            try {
                annotations = value.getClass().getDeclaredField(descriptor.getName()).getAnnotationsByType(FieldConverter.class);
            } catch (NoSuchFieldException e) {
                throw new ConvertException(e);
            }
            if (annotations == null || annotations.length == 0) {
                Converter converter = this.filter(fieldValue);
                if (converter != null) {
                    mapValue = converter.convert(fieldValue);
                }
            } else {
                if (fieldValue == null) {
                    continue;
                }
                Object v = mapValue;
                for (FieldConverter annotation : annotations) {
                    v = fieldConverterHandler.handler(annotation, v);
                }
                mapValue = v;
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
