package net.noboard.fastconverter;

/**
 * @author wanxm
 */
public interface Converter<T, K> {
    K convert(T value) throws ConvertException;

    K convert(T value, String tip) throws ConvertException;

    VerifyResult<K> convertAndVerify(T value) throws ConvertException;

    VerifyResult<K> convertAndVerify(T value, String tip) throws ConvertException;

    VerifyResult<K> convertAndVerify(T value, Validator afterConvert) throws ConvertException;

    VerifyResult<K> convertAndVerify(T value, String tip, Validator afterConvert) throws ConvertException;

    boolean supports(Object value);
}
