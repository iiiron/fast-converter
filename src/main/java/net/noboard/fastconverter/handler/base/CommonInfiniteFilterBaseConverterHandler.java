package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;

/**
 * 无限转换转换器
 *
 * 该转换器将使用ConverterFilter中注册的转换器进行无限次数的转换，直到没有适配的
 * 转换器可用为止。由于此转换器会进行无限次数的转换，所以你要确保你的ConverterFilter
 * 链路中，一定会转换出一种没有任何转换器可以对它继续进行转换的数据类型，且要保证
 * 不出现两个转换器互相supports各自转换结果的情况。
 *
 * @param <T>
 * @param <K>
 * @author wanxm
 */
public class CommonInfiniteFilterBaseConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<T, K> {

    public CommonInfiniteFilterBaseConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected K converting(T value, String tip) throws ConvertException {
        Object obj = value;
        Converter converter;
        while ((converter = this.getConverter(obj)) != null) {
            obj = converter.convert(obj);
        }
        return (K) obj;
    }

    @Override
    public boolean supports(Object value) {
        return this.getConverter(value) != null;
    }
}
