package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.parser.ConvertibleMap;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.beans.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于数据源上的注解来指导转换过程的bean转换器
 *
 * @date 2020/11/1 12:42 下午
 * @author by wanxm
 */
public class SourceBaseBeanConverterHandler extends AbstractBeanConverter<Object, Object> {

    public SourceBaseBeanConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    protected Map<String, Object> parse(Object source, String group, Class<?> target) {
        BeanInfo sourceBeanInfo;
        try {
            sourceBeanInfo = Introspector.getBeanInfo(source.getClass());
        } catch (IntrospectionException e) {
            throw new ConvertException(e);
        }

        Map<String, Object> result = new HashMap<>();
        for (PropertyDescriptor sourcePD : sourceBeanInfo.getPropertyDescriptors()) {
            if ("class".equals(sourcePD.getName())) {
                continue;
            }

            Field field;
            try {
                field = source.getClass().getDeclaredField(sourcePD.getName());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new ConvertException(String.format("field named '%s' not exist in %s",
                        sourcePD.getName(),
                        source.getClass().getName()));
            }

            ConvertibleMap currentMap = ConvertibleAnnotatedUtils.parse(field, group);
            ConvertibleMap last = lastConvertibleField(currentMap);
            String nameTo = sourcePD.getName();
            if (last != null) {
                if (last.isAbandon()) {
                    continue;
                }
                if (last.getNameTo() != null && !"".equals(last.getNameTo())) {
                    nameTo = last.getNameTo();
                }
            }

            try {
                result.put(nameTo, convertValue(sourcePD.getReadMethod().invoke(source), currentMap));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ConvertException(e);
            }
        }

        return result;
    }

    @Override
    public boolean supports(Object value, String group) {
        if (value == null) {
            return false;
        }
        Class<?> clazz = value.getClass();
        ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(clazz, group);
        return convertibleBean != null;
    }
}
