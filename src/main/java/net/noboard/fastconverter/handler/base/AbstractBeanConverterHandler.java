package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<T, K> {

    //TODO 找一个线程安全的list实现
    private static List<Converter> converters = new ArrayList<>();

    //TODO 找一个线程安全的list实现
    private static List<Validator> validators = new ArrayList<>();

    public AbstractBeanConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    public AbstractBeanConverterHandler(ConverterFilter converterFilter, String tip) {
        super(converterFilter, tip);
    }

    protected Object handler(ConverterIndicator annotation, Object value) {
        // 获取转换器
        Converter cc = getConverter(annotation);

        if ("".equals(annotation.tip())) {
            return cc.convert(value);
        } else {
            return cc.convert(value, annotation.tip());
        }
    }

    protected VerifyResult handlerAndVerify(ConverterIndicator annotation, Object fieldValue) {
        Object value = this.handler(annotation, fieldValue);

        Validator vv = getValidator(annotation);
        if (vv != null) {
            VerifyInfo verifyInfo = vv.validate(value);
            return new VerifyResult(value, verifyInfo.getErrCode(), verifyInfo.getErrMessage());
        } else {
            return new VerifyResult(value);
        }
    }

    protected Converter getConverter(ConverterIndicator annotation) {
        Converter cc = null;
        for (Converter converter : converters) {
            if (converter.getClass().equals(annotation.converter())) {
                cc = converter;
                break;
            }
        }
        if (cc == null) {
            try {
                cc = annotation.converter().newInstance();
                converters.add(cc);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ConvertException("创建" + annotation.converter().getName() + "实例失败：", e);
            }
        }
        return cc;
    }

    protected Validator getValidator(ConverterIndicator annotation) {
        Validator vv = null;
        for (Validator validator : validators) {
            if (validator.getClass().equals(annotation.afterConvert())) {
                vv = validator;
            }
        }
        if (vv == null) {
            try {
                if (!annotation.afterConvert().isInterface()) {
                    vv = annotation.afterConvert().newInstance();
                    validators.add(vv);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ConvertException("创建" + annotation.converter().getName() + "实例失败：", e);
            }
        }
        return vv;
    }

}
