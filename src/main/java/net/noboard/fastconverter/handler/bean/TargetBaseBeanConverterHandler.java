package net.noboard.fastconverter.handler.bean;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.handler.auto.AutoSensingConverter;
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
            while (true) {

                PropertyDescriptor propertyDescriptor = sourcePDMap.get(StringUtils.isEmpty(currentMap.getAliasName()) ? anchorPD.getName() : currentMap.getAliasName());

                if (!currentMap.isAbandon() && propertyDescriptor != null) {
                    // 读取源值
                    Object sourceValue;

                    if (result.containsKey(anchorPD.getName())) {
                        sourceValue = result.get(anchorPD.getName());
                    } else {
                        try {
                            sourceValue = propertyDescriptor.getReadMethod().invoke(source);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                            throw new ConvertException(e);
                        }
                    }

                    if (!ConvertibleMap.hasConverter(currentMap)) {
                        // 如果字段上没有指定转换器
                        // 检查当前字段类型是否标注了目标，有的话给上下文推一组映射
                        Class<?> fieldType = TypeProbeUtil.find(field);
                        ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(fieldType,
                                Converter.isTipHasMessage(currentMap.getTip()) ? currentMap.getTip() : Converter.DEFAULT_GROUP);

                        if (convertibleBean != null && convertibleBean.type() == ConvertibleBeanType.TARGET) {
                            Class<?> fieldTarget = ConvertibleAnnotatedUtils.getTargetClass(convertibleBean);
                            BeanMapping.push(fieldTarget, fieldType);
                            result.put(anchorPD.getName(), convertValue(sourceValue, group, currentMap));
                            BeanMapping.pop();
                        } else if (autoSensingConverter.supports(sourceValue, field.getGenericType().getTypeName())) {
                            // 当前目标没有标注ConvertibleBean，尝试进行转换
                            Object targetValue = autoSensingConverter.convert(sourceValue, field.getGenericType().getTypeName());
                            result.put(anchorPD.getName(), targetValue);
                        } else {
                            result.put(anchorPD.getName(), convertValue(sourceValue, group, currentMap));
                        }
                    } else {
                        // 字段上指定了转换器，则用指定的转换器来处理数据
                        result.put(anchorPD.getName(), convertValue(sourceValue, group, currentMap));
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

        BeanMapping beanMapping = BeanMapping.current();
        if (beanMapping == null || beanMapping.getSource() == null) {
            return false;
        }

        return beanMapping.getSource() == value.getClass();
    }
}
