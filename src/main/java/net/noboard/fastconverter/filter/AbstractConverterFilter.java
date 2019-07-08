package net.noboard.fastconverter.filter;

import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.handler.ArrayToListConverterHandler;
import net.noboard.fastconverter.handler.BeanToMapConverterHandler;
import net.noboard.fastconverter.handler.CollectionToListConverterHandler;
import net.noboard.fastconverter.handler.MapToMapConverterHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换器过滤器
 *
 * 注意：该转换器过滤器使用List<Converter<?, ?>>存储转换器，
 * 这将导致转换器丢失泛型信息。
 *
 * @author wanxm
 */
public abstract class AbstractConverterFilter implements ConverterFilter {

    private List<Converter<?, ?>> converters;

    public AbstractConverterFilter() {
        converters = new ArrayList<>();
        this.initConverters(converters);
    }

    protected abstract void initConverters(List<Converter<?, ?>> converters);

    protected void addContainerConverter() {
        converters.add(new MapToMapConverterHandler<>(this));
        converters.add(new ArrayToListConverterHandler<>(this));
        converters.add(new CollectionToListConverterHandler<>(this));
        converters.add(new BeanToMapConverterHandler(this));
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
}
