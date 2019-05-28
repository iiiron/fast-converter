package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.Field;
import net.noboard.fastconverter.FieldConverter;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 用以将对象转换为map
 *
 * @return
 * @author wanxm
 */
public class BeanToMapConverterHandler extends AbstractFilterBaseConverterHandler<Object, Map<String,Object>> {

    private static List<Converter> converters = new ArrayList<>();

    public BeanToMapConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected Map<String, Object> converting(Object value, String tip) throws ConvertException {
        Map<String, Object> map = new HashMap<>();

        for (java.lang.reflect.Field field : value.getClass().getDeclaredFields()) {
            // 对域名 及 是否隐藏的处理
            String mapKey;
            Field docField = field.getAnnotation(Field.class);
            if (docField != null) {
                if (docField.isHide()) {
                    continue;
                }

                mapKey = "".equals(docField.name())
                        ? field.getName()
                        : docField.name();
            } else {
                mapKey = field.getName();
            }

            // 获取域值
            Object fieldValue;
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), value.getClass());
                Method reader = pd.getReadMethod();
                if (reader != null) {
                    fieldValue = reader.invoke(value);
                } else {
                    continue;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new ConvertException("ObjectToMapConverterHandler对数据转换过程中发生异常：" + e.getMessage());
            } catch (IntrospectionException e) {
                continue;
            }

            // 转换域值
            Object mapValue = fieldValue;
            FieldConverter annotation = field.getAnnotation(FieldConverter.class);
            if (annotation == null) {
                Converter converter = this.getConverter(fieldValue);
                if (converter != null) {
                    mapValue = converter.convert(fieldValue);
                }
            } else {
                try {
                    boolean isConvert = false;
                    for (Converter converter : converters) {
                        if (converter.getClass().equals(annotation.converter())) {
                            mapValue = converter.convert(fieldValue, annotation.tip());
                            isConvert = true;
                            break;
                        }
                    }
                    if (!isConvert) {
                        Converter converter = annotation.converter().newInstance();
                        converters.add(converter);
                        mapValue = converter.convert(fieldValue, annotation.tip());
                    }

                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                    throw new ConvertException(e.getMessage());
                }
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
