package net.noboard.fastconverter;

import java.util.List;

/**
 * @author wanxm
 */
public interface Converter<T, K> {
    K convert(T value) throws ConvertException;

    K convert(T value, String tip) throws ConvertException;

    K convert(T value, Validator afterConvert) throws ConvertException;

    K convert(T value, String tip, Validator afterConvert) throws ConvertException;

    boolean supports(Object value);

    List<VerifyInfo> verifyInfos();
}
