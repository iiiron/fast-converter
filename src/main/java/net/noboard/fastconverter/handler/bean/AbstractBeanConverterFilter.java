package net.noboard.fastconverter.handler.bean;

import net.noboard.fastconverter.ConverterFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractBeanConverterFilter implements BeanConverterFilter {

    List<BeanConverter> beanConverters = new ArrayList<>();

    ConverterFilter converterFilter;

    public AbstractBeanConverterFilter(ConverterFilter converterFilter) {
        this.converterFilter = converterFilter;
        initConverters(beanConverters);
    }

    @Override
    public BeanConverter filter(Object value, String group) {
        for (BeanConverter converter : beanConverters) {
            if (converter.supports(value, group)) {
                return converter;
            }
        }

        return null;
    }

    @Override
    public void setConverterFilter(ConverterFilter converterFilter) {
        this.converterFilter = converterFilter;
    }

    @Override
    public ConverterFilter getConverterFilter() {
        return this.converterFilter;
    }

    @Override
    public BeanConverterFilter addFirst(BeanConverter converter) {
        beanConverters.add(0, converter);
        return this;
    }

    @Override
    public BeanConverterFilter addFirst(Function<ConverterFilter, BeanConverter> add) {
        beanConverters.add(0, add.apply(converterFilter));
        return this;
    }

    @Override
    public BeanConverterFilter addLast(BeanConverter converter) {
        beanConverters.add(converter);
        return this;
    }

    @Override
    public BeanConverterFilter addLast(Function<ConverterFilter, BeanConverter> add) {
        beanConverters.add(add.apply(converterFilter));
        return this;
    }

    protected abstract void initConverters(List<BeanConverter> converters);
}
