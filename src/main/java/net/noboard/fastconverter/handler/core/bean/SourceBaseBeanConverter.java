package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.parser.ConvertibleMap;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;
import net.noboard.fastconverter.support.TypeProbeUtil;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.beans.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于数据源上的注解来指导转换过程的bean转换器
 *
 * @date 2020/11/1 12:42 下午
 * @author by wanxm
 */
public class SourceBaseBeanConverter extends AbstractBeanConverter<Object, Object> {

    public SourceBaseBeanConverter(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    protected Map<String, Object> parse(Object source, String group) {
        BeanInfo anchorBeanInfo;
        BeanInfo sourceBeanInfo;
        try {
            anchorBeanInfo = Introspector.getBeanInfo(BeanMapping.current().getSource());
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

            // 解析当前字段的类型
            Class<?> fieldType = TypeProbeUtil.find(field);

            // 检查当前字段类型是否标注了目标，有的话给上下文推一组映射
            boolean isPushMapping = false;
            ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(fieldType, group);
            if (convertibleBean != null && convertibleBean.type() == ConvertibleBeanType.SOURCE) {
                Class<?> targetClass = null;
                try {
                    if (convertibleBean.targetClass() != Void.class) {
                        targetClass = convertibleBean.targetClass();
                    } else {
                        targetClass = Class.forName(convertibleBean.targetName());
                    }
                    isPushMapping = true;
                    BeanMapping.push(fieldType, targetClass);
                } catch (ClassNotFoundException e) {
                    throw new ConvertException(String.format("", ""), e);
                }
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
                result.put(nameTo, getConvertedValue(sourcePD.getReadMethod().invoke(source), currentMap));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ConvertException(e);
            } finally {
                // 弹出映射
                if (isPushMapping) {
                    BeanMapping.pop();
                }
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
