package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.handler.core.AbstractFilterBaseConverterHandler;
import org.springframework.core.annotation.AnnotatedElementUtils;

public class ConvertibleBeanConverterHandler extends AbstractFilterBaseConverterHandler<Object, Object> {

    private BeanConverterFilter beanConverterFilter;

    public ConvertibleBeanConverterHandler(ConverterFilter converterFilter) {
        this(converterFilter, Convertible.defaultGroup);
    }

    public ConvertibleBeanConverterHandler(ConverterFilter converterFilter, String group) {
        super(converterFilter, group);
    }

    public Object convert(Object value, Class clazz) {
        return this.convert(value, clazz.getName());
    }

    public void setBeanConverterFilter(BeanConverterFilter beanConverterFilter) {
        this.beanConverterFilter = beanConverterFilter;
    }

    public BeanConverterFilter getBeanConverterFilter() {
        return beanConverterFilter;
    }

    @Override
    protected Object converting(Object value, String group) throws ConvertException {
        return beanConverterFilter.filter(value, group).convert(value, group);
    }

    @Override
    public boolean supports(Object value) {
        if (value == null) {
            return false;
        } else {
            return AnnotatedElementUtils.getMergedAnnotation(value.getClass(), ConvertibleBean.class) != null
                    || AnnotatedElementUtils.getMergedAnnotationAttributes(value.getClass(), ConvertibleBeans.class) != null;
        }
    }
}
