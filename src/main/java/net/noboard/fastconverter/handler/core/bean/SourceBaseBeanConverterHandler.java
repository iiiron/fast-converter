package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.handler.AutoSensingConverter;
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

            // 查找那个字段
            Field field = FieldFindUtil.find(source.getClass(), sourcePD.getName());
            if (field == null) {
                throw new ConvertException(String.format("field named '%s' not exist in %s",
                        sourcePD.getName(),
                        source.getClass().getName()));
            }

            // 解析转换链
            ConvertibleMap currentMap = ConvertibleAnnotatedUtils.parse(field, group);
            while (true) {
                if (!currentMap.isAbandon()) {
                    String nameTo = sourcePD.getName();

                    if (currentMap.getAliasName() != null && !"".equals(currentMap.getAliasName())) {
                        nameTo = currentMap.getAliasName();
                    }

                    // 读取源值
                    Object sourceValue;
                    try {
                        sourceValue = sourcePD.getReadMethod().invoke(source);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        throw new ConvertException(e);
                    }

                    // 尝试进行自动识别转换
                    Field targetField = FieldFindUtil.find(target, nameTo);
                    if (!ConvertibleMap.hasConverter(currentMap)
                            && targetField != null
                            && autoSensingConverter.supports(sourceValue, targetField.getGenericType().getTypeName())) {
                        Object targetValue = autoSensingConverter.convert(sourceValue, targetField.getGenericType().getTypeName());
                        result.put(nameTo, targetValue);
                    } else {
                        result.put(nameTo, convertValue(sourceValue, currentMap));
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
