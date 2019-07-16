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

    public AbstractConverterHandler() {
        this.defaultTip = "";
    }

    public AbstractConverterHandler(String defaultTip) {
        this.defaultTip = defaultTip;
    }

    protected abstract K converting(T value, String tip) throws ConvertException;

    protected VerifyResult<K> converting(T value, String tip, Validator afterConvert) throws ConvertException {
        Objects.requireNonNull(afterConvert);
        K k = converting(value, tip);
        VerifyInfo verifyInfo = afterConvert.validate(k);
        if (verifyInfo.isPass()) {
            return new VerifyResult<>(k);
        } else {
            return new VerifyResult<>(k,verifyInfo.getErrCode(),verifyInfo.getErrMessage());
        }
    }

    protected String getDefaultTip() {
        return this.defaultTip;
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
    public VerifyResult<K> convert(T value, Validator afterConvert) throws ConvertException {
        return this.convert(value, this.defaultTip, afterConvert);
    }

    @Override
    public VerifyResult<K> convert(T value, String tip, Validator afterConvert) throws ConvertException {
        if (!this.supports(value)) {
            throw new ConvertException(MessageFormat.format("转换器 {0} 不匹配数据 {1}", this.getClass().getName(), value));
        }
        return this.converting(value, tip, afterConvert);
    }

    @Override
    public List<VerifyResult> verifyInfos() {
        return null;
    }
}
