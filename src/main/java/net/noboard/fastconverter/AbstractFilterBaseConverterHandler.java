package net.noboard.fastconverter;

import net.noboard.fastconverter.AbstractConverterHandler;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.FilterBaseConverter;

/**
 * 基于转换器过滤器的转换器
 *
 * 将T -> K
 *
 * @author wanxm
 */
public abstract class AbstractFilterBaseConverterHandler<T, K> extends AbstractConverterHandler<T, K> implements FilterBaseConverter {

    private ConverterFilter converterFilter;

    public AbstractFilterBaseConverterHandler(ConverterFilter converterFilter) {
        super();
        this.converterFilter = converterFilter;
    }

    public AbstractFilterBaseConverterHandler(ConverterFilter converterFilter, String tip) {
        super(tip);
        this.converterFilter = converterFilter;
    }

    @Override
    public ConverterFilter getFilter() {
        return converterFilter;
    }

    protected Converter filter(Object value) {
        return converterFilter.filter(value);
    }

    protected ConverterFilter getConverterFilter() {
        return converterFilter;
    }
}
