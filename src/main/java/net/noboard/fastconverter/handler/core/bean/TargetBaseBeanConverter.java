package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.*;
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
 * @date 2020/11/1 12:43 下午
 * @author by wanxm
 */
public class TargetBaseBeanConverter extends AbstractBeanConverter<Object, Object> {

    public TargetBaseBeanConverter(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    protected Map<String, Object> parse(Object source, String group) {
        BeanInfo anchorBeanInfo;
        BeanInfo sourceBeanInfo;
        try {
            anchorBeanInfo = Introspector.getBeanInfo(BeanMapping.current().getTarget());
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
                field = BeanMapping.current().getTarget().getDeclaredField(anchorPD.getName());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new ConvertException(String.format("field named '%s' not exist in %s",
                        anchorPD.getName(),
                        source.getClass().getName()));
            }

            ConvertibleMap currentMap = ConvertibleAnnotatedUtils.parse(field, group);
            ConvertibleMap last = lastConvertibleField(currentMap);

            if (last.isAbandon()) {
                continue;
            }

            // 解析当前字段的类型
            Class<?> fieldType = TypeProbeUtil.find(field);

            // 检查当前字段类型是否标注了目标，有的话给上下文推一组映射
            boolean isPushMapping = false;
            ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(fieldType,
                    Converter.isTipHasMessage(last.getTip()) ? last.getTip() : Converter.DEFAULT_GROUP);
            if (convertibleBean != null && convertibleBean.type() == ConvertibleBeanType.TARGET) {
                try {
                    isPushMapping = true;
                    BeanMapping.push(
                            convertibleBean.targetClass() == Void.class
                                    ? Class.forName(convertibleBean.targetName())
                                    : convertibleBean.targetClass(), fieldType);
                } catch (ClassNotFoundException e) {
                    throw new ConvertException(String.format("", ""), e);
                }
            }

            PropertyDescriptor sourcePD =
                    sourcePDMap.get(StringUtils.isEmpty(last.getNameTo()) ? anchorPD.getName() : last.getNameTo());

            try {
                result.put(anchorPD.getName(), getConvertedValue(sourcePD.getReadMethod().invoke(source), currentMap));
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

        BeanMapping beanMapping = BeanMapping.current();
        Class<?> clazz = beanMapping.getTarget();
        if (beanMapping.getSource() != value.getClass()) {
            return false;
        }
        ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(clazz, group);
        if (convertibleBean != null && convertibleBean.type() == ConvertibleBeanType.TARGET) {
            return beanMapping.getSource() == ConvertibleAnnotatedUtils.getTargetClass(convertibleBean);
        }

        return false;
    }
}
