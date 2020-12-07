package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.parser.ConvertibleMap;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;
import net.noboard.fastconverter.support.FieldFindUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * bean转map的转换器
 *
 * @date 2020/12/7 11:03 上午
 * @author by wanxm
 */
public class BeanToMapConverterHandler extends AbstractBeanConverter<Object, Object>{

    public BeanToMapConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    public Object convert(Object from, String group) throws ConvertException {
        return this.parse(from, group, getTargetClass(from, group));
    }

    @Override
    protected Map<String, Object> parse(Object source, String group, Class<?> target) {
        BeanInfo sourceBeanInfo;
        try {
            sourceBeanInfo = Introspector.getBeanInfo(source.getClass());
        } catch (IntrospectionException e) {
            throw new ConvertException(e);
        }

        Map<String, Object> result = (Map<String, Object>) instantiateTarget(target);
        for (PropertyDescriptor sourcePD : sourceBeanInfo.getPropertyDescriptors()) {
            if ("class".equals(sourcePD.getName())) {
                continue;
            }

            Field field = FieldFindUtil.find(source.getClass(), sourcePD.getName());
            if (field == null) {
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
        return convertibleBean != null && Map.class.isAssignableFrom(ConvertibleAnnotatedUtils.getTargetClass(convertibleBean));
    }
}
