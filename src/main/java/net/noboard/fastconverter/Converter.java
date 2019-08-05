package net.noboard.fastconverter;

/**
 * @author wanxm
 */
public interface Converter<T, K> {
    /**
     * 不进行验证的转换，且使用默认提示信息
     * @param value
     * @return
     * @throws ConvertException
     */
    K convert(T value) throws ConvertException;

    /**
     * 不进行验证的转换，且使用指定的提示信息
     * @param value
     * @param tip
     * @return
     * @throws ConvertException
     */
    K convert(T value, String tip) throws ConvertException;

    /**
     * 对参数value，转换后返回值进行校验，且使用默认提示信息
     * @param value
     * @return
     * @throws ConvertException
     */
    ConvertResult<K> convertAndVerify(T value) throws ConvertException;

    /**
     * 对参数value，转换后返回值进行校验，且使用指定的提示信息
     * @param value
     * @param tip
     * @return
     * @throws ConvertException
     */
    ConvertResult<K> convertAndVerify(T value, String tip) throws ConvertException;

    /**
     * 检查转换器是否支持该数据的转换。
     * @param value
     * @return
     */
    boolean supports(Object value);
}
