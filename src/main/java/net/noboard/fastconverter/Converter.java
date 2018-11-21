package net.noboard.fastconverter;

/**
 * @author wanxm
 */
public interface Converter<T, K> {
    K convert(T value, String tip) throws ConvertException;

    K convert(T value) throws ConvertException;

    boolean supports(T value);
}
