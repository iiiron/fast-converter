package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractFilterBaseConverterHandler;
import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

/**
 * 通用 T -> K 转换器
 *
 * @author wanxm
 */
public class CommonFilterBaseConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<T, K> {

    public CommonFilterBaseConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    public boolean supports(Object value, String tip) {
        return this.filter(value) != null;
    }

    @Override
    protected K converting(T value, String tip) throws ConvertException {
        Converter converter = this.filter(value);
        if (converter != null) {
            return (K) (Converter.isTipHasMessage(tip) ? converter.convert(value, tip) : converter.convert(value));
        } else {
            return (K) value;
        }
    }
}
