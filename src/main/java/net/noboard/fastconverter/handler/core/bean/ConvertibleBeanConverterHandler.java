package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.handler.core.AbstractFilterBaseConverterHandler;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.List;

public class ConvertibleBeanConverterHandler extends AbstractFilterBaseConverterHandler<Object, Object> {

    private final BeanConverterFilter beanConverterFilter;

    public ConvertibleBeanConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter, Converter.DEFAULT_GROUP);
        this.beanConverterFilter = new AbstractBeanConverterFilter(converterFilter) {
            @Override
            protected void initConverters(List<BeanConverter> converters) {
                converters.add(new TargetBaseBeanConverter(this.converterFilter));
                converters.add(new SourceBaseBeanConverter(this.converterFilter));
            }
        };
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
                    || (BeanMapping.hasMapping() && BeanMapping.current().getSource() == value.getClass());
        }
    }
}
