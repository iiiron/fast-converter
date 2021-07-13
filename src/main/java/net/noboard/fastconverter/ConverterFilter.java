package net.noboard.fastconverter;

import java.util.function.Function;

/**
 * 转换器过滤器
 *
 * @author wanxm
 */
public interface ConverterFilter {
    /**
     * 由于java泛型的擦除机制，你并不能根据Converter的泛型信息对它们进行类型上的区分。
     * 所以你也不能在筛选的过程中
     * @param value
     * @return
     */
    Converter filter(Object value);

    Converter filter(Object value, String tip);

    ConverterFilter addFirst(Converter converter);

    ConverterFilter addFirst(Function<ConverterFilter,Converter> add);

    ConverterFilter addLast(Converter converter);

    ConverterFilter addLast(Function<ConverterFilter,Converter> add);
}
