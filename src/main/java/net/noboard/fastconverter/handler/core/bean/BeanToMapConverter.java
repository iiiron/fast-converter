package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.util.Map;

public class BeanToMapConverter extends AbstractBeanConverter<Object, Map> {

    public BeanToMapConverter(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    public Map convert(Object value, String group) throws ConvertException {
        return parse(value, group);
    }

    @Override
    public boolean supports(Object value, String group) {
        ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(value.getClass(), group);
        Class clazz;
        if (!"".equals(convertibleBean.targetName())) {
            try {
                clazz = Class.forName(convertibleBean.targetName());
            } catch (ClassNotFoundException e) {
                throw new ConvertException(String.format("can not find class of %s, at @ConvertibleBean of %s",
                        convertibleBean.targetName(), value.getClass().getName()));
            }
        } else {
            clazz = convertibleBean.targetClass();
        }
        return Map.class.isAssignableFrom(clazz);
    }
}
