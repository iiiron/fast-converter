package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.handler.support.ConverterExceptionHelper;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * 数组到数组转换器
 * <p>
 * 该转换器会产生一个可以放置转换后数据的新数组，并使用ConverterFilter中注册的转换器对
 * 原数组中的元素一一进行转换，转换结果推入新数组中。如果过滤器没有找到可用的转换器则直接
 * 将原数据推入新数组中
 * <p>
 * 注意，由于java存在自动装箱，int[]经过转换后将变为Integer[]，其他基本数据类型同理。
 */
public class ArrayToArrayConverterHandler extends AbstractFilterBaseConverterHandler<Object, Object> {

    public ArrayToArrayConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        return this.convertingAndVerify(value, tip, null).getValue();
    }

    @Override
    protected VerifyResult<Object> convertingAndVerify(Object value, String tip, Validator afterConvert) throws ConvertException {
        Converter converter = null;
        Object newArray = null, newV = null, oldV = null;
        Map<String, VerifyInfo> errMap = new HashMap<>();
        try {
            for (int index = 0; index < Array.getLength(value); index++) {
                oldV = Array.get(value, index);
                newV = oldV;
                converter = this.filter(newV);
                if (converter != null) {
                    VerifyResult verifyResult = converter.convertAndVerify(newV, tip);
                    newV = verifyResult.getValue();
                    if (!verifyResult.isPass()) {
                        errMap.put(String.valueOf(index), verifyResult);
                    }
                }
                if (newArray == null && newV != null) {
                    newArray = Array.newInstance(newV.getClass(), Array.getLength(value));
                }
                if (newArray != null) {
                    Array.set(newArray, index, newV);
                }
            }
        } catch (Exception e) {
            ConverterExceptionHelper.factory(value, oldV, newArray, newV, converter, e);
        }

        VerifyInfo verifyInfo = null;
        if (afterConvert != null) {
            verifyInfo = afterConvert.validate(newArray);
            if (!verifyInfo.isPass()) {
                errMap.put("Array整体校验结果", verifyInfo);
            }
        }

        if (errMap.size() > 0) {
            if (verifyInfo != null) {
                return new VerifyResult<>(newArray, verifyInfo.getErrCode(), errMap.toString());
            } else {
                return new VerifyResult<>(newArray, errMap.toString());
            }
        } else {
            return new VerifyResult<>(newArray);
        }
    }

    @Override
    public boolean supports(Object value) {
        return value != null && value.getClass().isArray();
    }
}
