package net.noboard.fastconverter.handler.base;


import net.noboard.fastconverter.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

/**
 * @param <T>
 * @param <K>
 * @author wanxm
 */
public abstract class AbstractConverterHandler<T, K> implements Converter<T, K> {

    private String defaultTip;

    private Validator validator;

    public AbstractConverterHandler() {
        this.defaultTip = "";
        this.validator = null;
    }

    public AbstractConverterHandler(String defaultTip) {
        this.defaultTip = defaultTip;
    }

    public AbstractConverterHandler(Validator validator) {
        this.defaultTip = "";
        this.validator = validator;
    }

    public AbstractConverterHandler(String defaultTip, Validator validator) {
        this.defaultTip = defaultTip;
        this.validator = validator;
    }

    /**
     *
     * @param value
     * @param tip 要么是有效的用户传入的tip，要么是defaultTip
     * @return
     * @throws ConvertException
     */
    protected abstract K converting(T value, String tip) throws ConvertException;

    /**
     *
     * @param value
     * @param tip 要么是有效的用户传入的tip，要么是defaultTip
     * @param afterConvert 要么是有效的用户传入的Validator，要么是默认的Validator
     * @return
     * @throws ConvertException
     */
    protected VerifyResult<K> convertingAndVerify(T value, String tip, Validator afterConvert) throws ConvertException {
        K k = converting(value, defaultTip(tip));
        Validator validator = defaultValidator(afterConvert);
        if (validator != null) {
            VerifyInfo verifyInfo = validator.validate(k);
            if (verifyInfo.isPass()) {
                return new VerifyResult<>(k);
            } else {
                return new VerifyResult<>(k, verifyInfo.getErrCode(), verifyInfo.getErrMessage());
            }
        } else {
            return new VerifyResult<>(k);
        }
    }

    protected String getDefaultTip() {
        return this.defaultTip;
    }

    protected Validator getDefaultValidator() {
        return this.validator;
    }

    @Override
    public K convert(T value) throws ConvertException {
        return this.convert(value, this.defaultTip);
    }

    @Override
    public K convert(T value, String tip) throws ConvertException {
        if (!this.supports(value)) {
            throw new ConvertException(MessageFormat.format("转换器 {0} 不匹配数据 {1}", this.getClass().getName(), value));
        }

        return this.converting(value, tip);
    }

    @Override
    public VerifyResult<K> convertAndVerify(T value) throws ConvertException {
        return this.convertAndVerify(value, this.defaultTip, this.validator);
    }

    @Override
    public VerifyResult<K> convertAndVerify(T value, String tip) throws ConvertException {
        return this.convertAndVerify(value, tip, this.validator);
    }

    @Override
    public VerifyResult<K> convertAndVerify(T value, Validator afterConvert) throws ConvertException {
        return this.convertAndVerify(value, this.defaultTip, afterConvert);
    }

    @Override
    public VerifyResult<K> convertAndVerify(T value, String tip, Validator afterConvert) throws ConvertException {
        if (!this.supports(value)) {
            throw new ConvertException(MessageFormat.format("转换器 {0} 不匹配数据 {1}", this.getClass().getName(), value));
        }
        return this.convertingAndVerify(value, tip, afterConvert);
    }

    protected String defaultTip(String tip) {
        return (tip == null || "".equals(tip)) ? this.defaultTip : tip;
    }

    protected Validator defaultValidator(Validator validator) {
        return validator == null ? this.validator : validator;
    }
}
