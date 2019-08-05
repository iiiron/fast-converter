package net.noboard.fastconverter.filter;

import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 转换器过滤器
 *
 * @author wanxm
 */
public abstract class AbstractConverterFilter implements ConverterFilter {

    private List<Converter> converters;

    public AbstractConverterFilter() {
        converters = new ArrayList<>();
        this.initConverters(converters);
    }

    protected abstract void initConverters(List<Converter> converters);

    protected void addContainerConverter(List<Converter> converters) {
//        converters.add(new CollectionToCollectionConverterHandler<>(this));
//        converters.add(new MapToMapConverterHandler<>(this));
//        converters.add(new ArrayToArrayConverterHandler(this));
    }

    @Override
    public Converter getConverter(Object value) {
        for (Converter converter : converters) {
            if (converter.supports(value)) {
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
        converters.add(add.apply(this));
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
