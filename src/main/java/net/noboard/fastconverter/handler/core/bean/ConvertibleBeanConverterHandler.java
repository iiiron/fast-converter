package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.handler.core.AbstractFilterBaseConverterHandler;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.List;

public class ConvertibleBeanConverterHandler extends AbstractFilterBaseConverterHandler<Object, Object> {

    private BeanConverterFilter beanConverterFilter;

    public ConvertibleBeanConverterHandler(ConverterFilter converterFilter) {
        this(converterFilter, Converter.DEFAULT_GROUP);
        this.beanConverterFilter = new AbstractBeanConverterFilter(converterFilter) {
            @Override
            protected void initConverters(List<BeanConverter> converters) {
//                converters.add(new BeanToMapConverter(defaultConverters));
                converters.add(new SourceBaseBeanConverter(this.converterFilter));
                converters.add(new TargetBaseBeanConverter(this.converterFilter));
            }
        };
    }

    public ConvertibleBeanConverterHandler(ConverterFilter converterFilter, String group) {
        super(converterFilter, group);
    }

    public Object convert(Object value, Class clazz) {
        return this.convert(value, clazz.getName());
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
                    || AnnotatedElementUtils.getMergedAnnotationAttributes(value.getClass(), ConvertibleBeans.class) != null
                    || BeanMapping.current().getSource() == value.getClass();
        }
    }
}
