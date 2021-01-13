package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.handler.AutoSensingConverter;
import net.noboard.fastconverter.parser.ConvertibleMap;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;
import net.noboard.fastconverter.support.TypeProbeUtil;
import org.springframework.util.StringUtils;

import java.beans.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于转换结果上的注解来指导转换过程的bean转换器
 *
 * @author by wanxm
 * @date 2020/11/1 12:43 下午
 */
public class TargetBaseBeanConverterHandler extends AbstractBeanConverter<Object, Object> {

    private static AutoSensingConverter autoSensingConverter = new AutoSensingConverter();

    public TargetBaseBeanConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    protected Map<String, Object> parse(Object source, String group, Class<?> target) {
        BeanInfo anchorBeanInfo;
        BeanInfo sourceBeanInfo;
        try {
            anchorBeanInfo = Introspector.getBeanInfo(target);
            sourceBeanInfo = Introspector.getBeanInfo(source.getClass());
        } catch (IntrospectionException e) {
            throw new ConvertException(e);
        }

        Map<String, PropertyDescriptor> sourcePDMap =
                Arrays.stream(sourceBeanInfo.getPropertyDescriptors()).collect(Collectors.toMap(FeatureDescriptor::getName, o -> o));

        Map<String, Object> result = new HashMap<>();
        for (PropertyDescriptor anchorPD : anchorBeanInfo.getPropertyDescriptors()) {
            if ("class".equals(anchorPD.getName())) {
                continue;
            }

            Field field;
            try {
                field = target.getDeclaredField(anchorPD.getName());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new ConvertException(String.format("field named '%s' not exist in %s",
                        anchorPD.getName(),
                        source.getClass().getName()));
            }

            ConvertibleMap currentMap = ConvertibleAnnotatedUtils.parse(field, group);
            ConvertibleMap last = ConvertibleMap.last(currentMap);

            if (last.isAbandon()) {
                continue;
            }

            // 读取源值
            Object sourceValue;

            PropertyDescriptor propertyDescriptor = sourcePDMap.get(StringUtils.isEmpty(last.getNameTo()) ? anchorPD.getName() : last.getNameTo());
            if (propertyDescriptor == null) {
                throw new ConvertException(String.format("field named '%s' not exist in %s",
                        StringUtils.isEmpty(last.getNameTo()) ? anchorPD.getName() : last.getNameTo(),
                        source.getClass().getName()));
            }

            try {
                sourceValue = propertyDescriptor.getReadMethod().invoke(source);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new ConvertException(e);
            }

            // 如果字段上没有指定转换器
            if (!ConvertibleMap.hasConverter(currentMap)) {
                // 检查当前字段类型是否标注了目标，有的话给上下文推一组映射
                Class<?> fieldType = TypeProbeUtil.find(field);
                ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(fieldType,
                        Converter.isTipHasMessage(last.getTip()) ? last.getTip() : Converter.DEFAULT_GROUP);
                if (convertibleBean != null && convertibleBean.type() == ConvertibleBeanType.TARGET) {
                    try {
                        BeanMapping.push(
                                convertibleBean.targetClass() == Void.class
                                        ? Class.forName(convertibleBean.targetName())
                                        : convertibleBean.targetClass(), fieldType);
                        try {
                            result.put(anchorPD.getName(), convertValue(sourceValue, currentMap));
                            continue;
                        } finally {
                            BeanMapping.pop();
                        }
                    } catch (ClassNotFoundException e) {
                        throw new ConvertException(String.format("", ""), e);
                    }
                } else if (autoSensingConverter.supports(sourceValue, field.getType().getName())) {
                    // 当前目标没有标注ConvertibleBean，尝试进行转换
                    Object targetValue = autoSensingConverter.convert(sourceValue, field.getType().getName());
                    result.put(anchorPD.getName(), targetValue);
                    continue;
                }

                result.put(anchorPD.getName(), convertValue(sourceValue, currentMap));
            } else {
                // 字段上指定了转换器，则用指定的转换器来处理数据
                result.put(anchorPD.getName(), convertValue(sourceValue, currentMap));
            }
        }

        return result;
    }

    @Override
    public boolean supports(Object value, String group) {
        if (value == null) {
            return false;
        }

        BeanMapping beanMapping = BeanMapping.current();
        if (beanMapping == null || beanMapping.getSource() == null) {
            return false;
        }

        return beanMapping.getSource() == value.getClass();
    }
}
