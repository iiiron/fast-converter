package net.noboard.fastconverter.filter;

import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

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

    @Override
    public <T> Converter getConverter(T value) {
        for (Converter converter : converters) {
            try {
                if (converter.supports(value)) {
                    return converter;
                }
            } catch (ClassCastException ignored) {

            }
        }

        return null;
    }
}
