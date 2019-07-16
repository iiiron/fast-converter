package net.noboard.fastconverter.handler.support;

import net.noboard.fastconverter.*;

import java.util.ArrayList;
import java.util.List;

public class FieldConverterHandler {

    //TODO 找一个线程安全的list实现
    private static List<Converter> converters = new ArrayList<>();

    //TODO 找一个线程安全的list实现
    private static List<FieldValidator> validators = new ArrayList<>();

    public Object handler(FieldConverter annotation, Object value) {
        // 获取转换器
        Converter cc = getConverter(annotation);

        if ("".equals(annotation.tip())) {
            return cc.convert(value);
        } else {
            return cc.convert(value, annotation.tip());
        }
    }

    public VerifyResult handlerAndVerify(FieldConverter annotation, Object field, Object bean) {
        // 获取验证器
        FieldValidator vv = getValidator(annotation);

        Object value = this.handler(annotation, field);
        VerifyInfo verifyInfo = vv.validate(value, bean);
        return new VerifyResult(value, verifyInfo.getErrCode(), verifyInfo.getErrMessage());
    }

    public Converter getConverter(FieldConverter annotation) {
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
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ConvertException("创建" + annotation.converter().getName() + "实例失败：", e);
            }
            converters.add(cc);
        }
        return cc;
    }

    public FieldValidator getValidator(FieldConverter annotation) {
        FieldValidator vv = null;
        for (FieldValidator validator : validators) {
            if (validator.getClass().equals(annotation.afterConvert())) {
                vv = validator;
            }
        }
        if (vv == null) {
            try {
                if (!annotation.afterConvert().isInterface()) {
                    vv = annotation.afterConvert().newInstance();
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ConvertException("创建" + annotation.converter().getName() + "实例失败：", e);
            }
            validators.add(vv);
        }
        return vv;
    }
}
