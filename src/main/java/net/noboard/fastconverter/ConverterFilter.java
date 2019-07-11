package net.noboard.fastconverter;

import java.util.function.Function;

/**
 * 转换器过滤器
 *
 * 筛选出能处理 T 数据的转换器
 *
 * 由于泛型擦除机制的存在，该接口就算限定 Converter<T, K> ,你也无法获取到
 * 正确的能将T -> K 的Converter<T, K>转换器。你获取到的仅仅是 Converter。
 *
 * @author wanxm
 */
public interface ConverterFilter {
    Converter getConverter(Object value);

    ConverterFilter addFirst(Converter converter);

    ConverterFilter addFirst(Function<ConverterFilter,Converter> add);

    ConverterFilter addLast(Converter converter);

    ConverterFilter addLast(Function<ConverterFilter,Converter> add);
}
