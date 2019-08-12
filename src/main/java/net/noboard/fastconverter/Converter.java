package net.noboard.fastconverter;

/**
 * @author wanxm
 */
public interface Converter<T, K> {

    String DEFAULT_GROUP = "default";

    String DEFAULT_TIP = "";

    static boolean isTipHasMessage(String tip) {
        return !Converter.DEFAULT_TIP.equals(tip) && tip != null;
    }



    K convert(T value, String tip) throws ConvertException;

    K convert(T value) throws ConvertException;

    void setDefaultTip(String tip);

    String getDefaultTip();

    boolean supports(Object value);
}
