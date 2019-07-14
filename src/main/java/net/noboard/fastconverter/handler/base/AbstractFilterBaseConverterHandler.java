package net.noboard.fastconverter.handler.base;

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
        super();
        this.converterFilter = converterFilter;
    }

    public AbstractFilterBaseConverterHandler(ConverterFilter converterFilter, String tip) {
        super(tip);
        this.converterFilter = converterFilter;
    }

    protected Converter filter(Object value) {
        return converterFilter.filter(value);
    }

    protected ConverterFilter getConverterFilter() {
        return converterFilter;
    }
}
