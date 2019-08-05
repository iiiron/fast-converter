package net.noboard.fastconverter.handler.base;


import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.ConvertResult;
import net.noboard.fastconverter.Converter;

import javax.validation.*;
import javax.validation.constraints.Max;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * @param <T>
 * @param <K>
 * @author wanxm
 */
public abstract class AbstractConverterHandler<T, K> implements Converter<T, K> {

    private String defaultTip;

    private ExecutableValidator executableValidator;

    public AbstractConverterHandler() {
        this("");
    }

    public AbstractConverterHandler(String defaultTip) {
        this.defaultTip = defaultTip;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        executableValidator = factory.getValidator().forExecutables();
    }

    /**
     * @param value
     * @param tip   要么是有效的用户传入的tip，要么是defaultTip
     * @return
     * @throws ConvertException
     */
    protected abstract K converting(@Max(1) T value, String tip) throws ConvertException;

    /**
     * @param value
     * @param tip   要么是有效的用户传入的tip，要么是defaultTip
     * @return
     * @throws ConvertException
     */
    protected ConvertResult<K> convertingAndVerify(T value, String tip) throws ConvertException {
        K k = converting(value, defaultTip(tip));
        ConvertResult<K> convertResult = new ConvertResult<>(k);
        try {
            Method method = this.getClass().getDeclaredMethod("converting", Object.class, String.class);
            for (ConstraintViolation<AbstractConverterHandler<T, K>> constraintValidator : executableValidator.validateParameters(this, method, new Object[]{value})) {
                convertResult.appendViolation(constraintValidator);
            }
            for (ConstraintViolation<AbstractConverterHandler<T, K>> constraintValidator : executableValidator.validateReturnValue(this, method, k)) {
                convertResult.appendViolation(constraintValidator);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return convertResult;
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
    public ConvertResult<K> convertAndVerify(T value) throws ConvertException {
        return this.convertAndVerify(value, this.defaultTip);
    }

    @Override
    public ConvertResult<K> convertAndVerify(T value, String tip) throws ConvertException {
        return this.convertingAndVerify(value, tip);
    }


    protected String defaultTip(String tip) {
        return (tip == null || "".equals(tip)) ? this.defaultTip : tip;
    }

}
