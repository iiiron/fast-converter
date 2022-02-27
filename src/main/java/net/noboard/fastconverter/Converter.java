package net.noboard.fastconverter;

/**
 * @author wanxm
 */
public interface Converter<T, K, V> {

    String DEFAULT_GROUP = "default";

    String DEFAULT_TIP = "";

    // TODO 这个方法也删掉
    static boolean isTipHasMessage(String tip) {
        return tip != null && !Converter.DEFAULT_TIP.equals(tip);
    }

    K convert(T value, V context) throws ConvertException;

    K convert(T value) throws ConvertException;
}
