package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

/**
 * 基于转换器过滤器的转换器
 *
 * 将T -> K
 *
 * @author wanxm
 */
public abstract class AbstractFilterBaseConverterHandler<T, K> extends AbstractConverterHandler<T, K> {

    private ConverterFilter converterFilter;

    public AbstractFilterBaseConverterHandler(ConverterFilter converterFilter) {
        this.converterFilter = converterFilter;
    }

    protected Converter getConverter(Object value) {
        return converterFilter.getConverter(value);
    }

    protected ConverterFilter getConverterFilter() {
        return converterFilter;
    }
}
