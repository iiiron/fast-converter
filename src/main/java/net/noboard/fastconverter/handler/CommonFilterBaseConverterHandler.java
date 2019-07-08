package net.noboard.fastconverter.handler;

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
    public boolean supports(Object value) {
        return this.getConverter(value) != null;
    }

    @Override
    protected K converting(T value, String tip) throws ConvertException {
        Converter converter = this.getConverter(value);
        if (converter != null) {
            return (K) converter.convert(value);
        } else {
            throw new ConvertException("没有转换器可以处理" + value);
        }
    }
}
