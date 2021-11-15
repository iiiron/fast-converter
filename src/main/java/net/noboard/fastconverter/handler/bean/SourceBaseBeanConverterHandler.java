package net.noboard.fastconverter.handler.bean;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.handler.auto.AutoSensingConverter;
import net.noboard.fastconverter.parser.ConvertibleMap;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;
import net.noboard.fastconverter.support.FieldFindUtil;

import java.beans.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于数据源上的注解来指导转换过程的bean转换器
 *
 * @author by wanxm
 * @date 2020/11/1 12:42 下午
 */
public class SourceBaseBeanConverterHandler extends AbstractBeanConverter<Object, Object> {

    private static AutoSensingConverter autoSensingConverter = new AutoSensingConverter();

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

            Field field = FieldFindUtil.find(source.getClass(), sourcePD.getName());

            // 解析转换链
            ConvertibleMap currentMap = ConvertibleAnnotatedUtils.parse(field, group);
            while (true) {
                if (!currentMap.isAbandon()) {
                    String aliasName = sourcePD.getName();

                    if (currentMap.getAliasName() != null && !"".equals(currentMap.getAliasName())) {
                        aliasName = currentMap.getAliasName();
                    }

                    // 读取源值
                    Object sourceValue;

                    if (result.containsKey(aliasName)) {
                        sourceValue = result.get(aliasName);
                    } else {
                        try {
                            sourceValue = sourcePD.getReadMethod().invoke(source);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                            throw new ConvertException(e);
                        }
                    }

                    Field targetField = FieldFindUtil.find(target, aliasName);
                    if (targetField == null) {
                        // 如果目标bean中没有找到指定的字段, 则跳过该字段
                    } else if (!ConvertibleMap.hasConverter(currentMap)
                            && autoSensingConverter.supports(sourceValue, targetField.getGenericType().getTypeName())) {
                        // 如果字段上没有指定转换器, 并且自动感知转换器适配该字段的转换, 则将该字段的转换交给自动感知转换器
                        Object targetValue = autoSensingConverter.convert(sourceValue, targetField.getGenericType().getTypeName());
                        result.put(aliasName, targetValue);
                    } else {
                        result.put(aliasName, convertValue(sourceValue, group, currentMap));
                    }
                }

                if (currentMap.hasNext()) {
                    currentMap = currentMap.next();
                } else {
                    break;
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
        return convertibleBean != null && ConvertibleBeanType.SOURCE.equals(convertibleBean.type());
    }
}
