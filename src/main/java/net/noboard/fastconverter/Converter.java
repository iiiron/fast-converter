package net.noboard.fastconverter;

/**
 * @author wanxm
 */
public interface Converter<T, K, V> {

    String DEFAULT_GROUP = "default";

    String DEFAULT_TIP = "";

    K convert(T value, V context) throws ConvertException;

    K convert(T value) throws ConvertException;
}
