package net.noboard.fastconverter.handler.bean;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.ConvertibleBeanType;
import net.noboard.fastconverter.AbstractConverterHandler;
import net.noboard.fastconverter.ConvertInfo;
import net.noboard.fastconverter.parser.ConvertibleMap;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;
import org.springframework.util.StringUtils;

import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class BeanConverterHandler<T, K> extends AbstractConverterHandler<T, K, ConvertInfo> {
    @Override
    protected K doConvert(T value, ConvertInfo context) {
        Object targetObj = instantiateTarget((Class<?>) context.getTargetType());

        Map<String, Object> map = parse(value, context);

        try {
            for (PropertyDescriptor targetPD : Introspector.getBeanInfo(targetObj.getClass()).getPropertyDescriptors()) {
                if ("class".equals(targetPD.getName())) {
                    continue;
                }
                try {
                    targetPD.getWriteMethod().invoke(targetObj, map.get(targetPD.getName()));
                } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                    throw new ConvertException(
                            String.format("Conversion result type mismatch. at field '%s' of target class %s", targetPD.getName(), targetObj.getClass().getName()));
                }
            }
        } catch (IntrospectionException e) {
            throw new ConvertException(e);
        }

        return (K) targetObj;
    }

    @Override
    protected ConvertInfo defaultContext() {
        return null;
    }

    protected Map<String, Object> parse(Object data, ConvertInfo convertInfo) {
        // data == null的控制逻辑, 校验转换所需要的source和data的类型能否对应上

        // 标注直接的class
        Class<?> tagClass = convertInfo.getModeType() == ConvertibleBeanType.SOURCE ? (Class<?>) convertInfo.getSourceType() : (Class<?>) convertInfo.getTargetType();
        // 相对的另一个class
        Class<?> relevantClass = convertInfo.getModeType() == ConvertibleBeanType.SOURCE ? (Class<?>) convertInfo.getTargetType() : (Class<?>) convertInfo.getSourceType();
        Map<String, Field> relevantFieldMap = getFields(relevantClass).stream().collect(Collectors.toMap(Field::getName, o -> o));
        Map<String, Field> fieldMap = getFields(tagClass).stream().collect(Collectors.toMap(Field::getName, o -> o, (b, a) -> b));

        Map<String, Object> result = new HashMap<>();

        PropertyDescriptor[] targetDescriptor = null, relevantDescriptors = null;
        try {
            targetDescriptor = Introspector.getBeanInfo(tagClass).getPropertyDescriptors();
            relevantDescriptors = Introspector.getBeanInfo(relevantClass).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        Map<String, PropertyDescriptor> relevantDPMap = Arrays.stream(relevantDescriptors).collect(Collectors.toMap(FeatureDescriptor::getName, o -> o, (b, a) -> b));

        for (PropertyDescriptor targetPD : targetDescriptor) {
            if ("class".equals(targetPD.getName())) {
                continue;
            }

            Field declaredField = fieldMap.get(targetPD.getName());

            ConvertibleMap currentMap = ConvertibleAnnotatedUtils.parse(declaredField, convertInfo.getGroup());

            // 结果集
            Map<String, Object> fieldResult = new HashMap<>();

            // 遍历转换地图
            while (true) {
                // 字段别名
                String aliasName = StringUtils.isEmpty(currentMap.getAliasName()) ? declaredField.getName() : currentMap.getAliasName();

                String fieldName = convertInfo.getModeType() == ConvertibleBeanType.SOURCE ? aliasName : declaredField.getName();

                // 找到关联类下的关联字段
                PropertyDescriptor relevantDP = relevantDPMap.get(aliasName);
                Field field = relevantFieldMap.get(aliasName);
                if (field != null && relevantDP != null) {
                    // 读取数据
                    Object fieldData = null;
                    if (!fieldResult.containsKey(fieldName)) {
                        try {
                            fieldData = convertInfo.getModeType() == ConvertibleBeanType.SOURCE ? targetPD.getReadMethod().invoke(data) : relevantDP.getReadMethod().invoke(data);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new ConvertException("获取数据失败");
                        }
                    } else {
                        fieldData = fieldResult.get(fieldName);
                    }

                    // 转换
                    if (fieldData != null || !currentMap.ignoreNull()) {
                        if (currentMap.getConverter() != null) {
                            fieldData = currentMap.getConverter().convert(fieldData, currentMap.getConvertContext());
                        } else {
                            ConvertInfo currentConvertInfo = new ConvertInfo();
                            currentConvertInfo.setModeType(convertInfo.getModeType());
                            currentConvertInfo.setConverterFilter(convertInfo.getConverterFilter());
                            currentConvertInfo.setGroup(convertInfo.getGroup());
                            currentConvertInfo.setSourceType(convertInfo.getModeType() == ConvertibleBeanType.SOURCE ? declaredField.getGenericType() : field.getGenericType());
                            currentConvertInfo.setTargetType(convertInfo.getModeType() == ConvertibleBeanType.SOURCE ? field.getGenericType() : declaredField.getGenericType());
                            fieldData = convertInfo.getConverterFilter().filter(fieldData, currentConvertInfo).convert();
                        }
                    }

                    fieldResult.put(fieldName, fieldData);
                }

                if (currentMap.hasNext()) {
                    currentMap = currentMap.next();
                } else {
                    result.putAll(fieldResult);
                    break;
                }
            }
        }

        return result;
    }

    private Object instantiateTarget(Class<?> target) {
        try {
            return target.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new ConvertException(
                    String.format("the target class %s can not be implemented", target), e);
        }
    }

    private List<Field> getFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        Class current = clazz;
        while (current != null) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields;
    }
}
