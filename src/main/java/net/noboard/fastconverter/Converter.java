package net.noboard.fastconverter;

/**
 * @author wanxm
 */
public interface Converter<T, K> {
    K convert(T value, String tip) throws ConvertException;

    K convert(T value) throws ConvertException;

    void setDefaultTip(String tip);

    String getDefaultTip();

    boolean supports(Object value);

    static boolean isTipHasMessage(String tip) {
        return !Convertible.defaultTip.equals(tip) && tip != null;
    }
}
