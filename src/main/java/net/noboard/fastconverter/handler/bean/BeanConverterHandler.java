package net.noboard.fastconverter.handler.bean;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.ConverteModeType;
import net.noboard.fastconverter.handler.AbstractConverterHandler;
import net.noboard.fastconverter.parser.ConvertibleMap;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;
import org.springframework.util.StringUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        // 标注直接的class
        Class<?> tagClass = convertInfo.getModeType() == ConverteModeType.SOURCE ? (Class<?>) convertInfo.getSourceType() : (Class<?>) convertInfo.getTargetType();
        // 相对的另一个class
        Class<?> relevantClass = convertInfo.getModeType() == ConverteModeType.SOURCE ? (Class<?>) convertInfo.getTargetType() : (Class<?>) convertInfo.getSourceType();

        Map<String, Field> relevantFieldMap = Arrays.stream(relevantClass.getDeclaredFields()).collect(Collectors.toMap(Field::getName, o -> o));

        Map<String, Object> result = new HashMap<>();

        for (Field declaredField : tagClass.getDeclaredFields()) {
            ConvertibleMap currentMap = ConvertibleAnnotatedUtils.parse(declaredField, convertInfo.getGroup());

            // 结果集
            Map<String, Object> fieldResult = new HashMap<>();

            // 遍历转换地图
            while (true) {
                // 字段别名
                String aliasName = StringUtils.isEmpty(currentMap.getAliasName()) ? declaredField.getName() : currentMap.getAliasName();

                String fieldName = convertInfo.getModeType() == ConverteModeType.SOURCE ? aliasName : declaredField.getName();

                // 找到关联类下的关联字段
                Field field = relevantFieldMap.get(aliasName);
                if (field != null) {
                    // 读取数据
                    Object fieldData = null;
                    if (!fieldResult.containsKey(fieldName)) {
                        declaredField.setAccessible(true);
                        field.setAccessible(true);
                        try {
                            fieldData = convertInfo.getModeType() == ConverteModeType.SOURCE ? declaredField.get(data) : field.get(data);
                        } catch (IllegalAccessException e) {
                            throw new ConvertException("获取数据失败");
                        }
                    } else {
                        fieldData = fieldResult.get(fieldName);
                    }

                    if (currentMap.getConverter() != null) {
                        fieldData = currentMap.getConverter().convert(fieldData, currentMap.getConvertContext());
                    } else {
                        ConvertInfo currentConvertInfo = new ConvertInfo();
                        currentConvertInfo.setModeType(convertInfo.getModeType());
                        currentConvertInfo.setConverterFilter(convertInfo.getConverterFilter());
                        currentConvertInfo.setGroup(convertInfo.getGroup());
                        currentConvertInfo.setSourceType(convertInfo.getModeType() == ConverteModeType.SOURCE ? declaredField.getGenericType() : field.getGenericType());
                        currentConvertInfo.setTargetType(convertInfo.getModeType() == ConverteModeType.SOURCE ? field.getGenericType() : declaredField.getGenericType());
                        fieldData = convertInfo.getConverterFilter().filter(fieldData, currentConvertInfo).convert();
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
}
