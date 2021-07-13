package net.noboard.fastconverter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 转换器过滤器
 *
 * @author wanxm
 */
public abstract class AbstractConverterFilter implements ConverterFilter {

    protected List<Converter> converters;

    public AbstractConverterFilter() {
        converters = new ArrayList<>();
        this.initConverters(converters);
    }

    protected abstract void initConverters(List<Converter> converters);

    @Override
    public Converter filter(Object value) {
        for (Converter converter : converters) {
            if (converter.supports(value)) {
                return converter;
            }
        }

        return null;
    }

    @Override
    public Converter filter(Object value, String tip) {
        for (Converter converter : converters) {
            if (converter.supports(value, tip)) {
                return converter;
            }
        }

        return null;
    }

    @Override
    public ConverterFilter addFirst(Converter converter) {
        converters.add(0, converter);
        return this;
    }

    @Override
    public ConverterFilter addFirst(Function<ConverterFilter, Converter> add) {
        converters.add(0, add.apply(this));
        return this;
    }

    @Override
    public ConverterFilter addLast(Converter converter) {
        converters.add(converter);
        return this;
    }

    @Override
    public ConverterFilter addLast(Function<ConverterFilter, Converter> add) {
        converters.add(add.apply(this));
        return this;
    }
}
