package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.handler.support.ConverterExceptionHelper;

import java.lang.reflect.Array;

/**
 * 数组到数组转换器
 * <p>
 * 该转换器会产生一个可以放置转换后数据的新数组，并使用ConverterFilter中注册的转换器对
 * 原数组中的元素一一进行转换，转换结果推入新数组中。如果过滤器没有找到可用的转换器则直接
 * 将原数据推入新数组中
 * <p>
 * 注意，由于java存在自动装箱，int[]经过转换后将变为Integer[]，其他基本数据类型同理。
 *
 * 关于该类的写法:
 *
 * public class ArrayToArrayConverterHandler<T,K> extends AbstractFilterBaseConverterHandler<T[], K[]>
 *      这种写法的问题是，当你实例化该类的实例时，不能使用 new ArrayToArrayConverterHandler<int,int>,java
 *      的泛型不支持这样的写法
 *
 * public class ArrayToArrayConverterHandler<T,K> extends AbstractFilterBaseConverterHandler<T, K>
 *     这种写法会在方法边界处发生强制类型转换，会导致int[]的数据经过转换后变成Integer[]的数据，而
 *     Integer[]不能被强制类型转换为int[]，则实例 new ArrayToArrayConverterHandler<int[],int[]> 一
 *     定发生类型转换异常
 *
 * 所以该类暂时采用了不使用泛型的写法。如果你有更好的想法，欢迎PR。
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
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (int index = 0; index < Array.getLength(value); index++) {
                oldV = Array.get(value, index);
                newV = oldV;
                converter = this.filter(newV);
                if (converter != null) {
                    VerifyResult verifyResult = converter.convertAndVerify(newV, tip);
                    newV = verifyResult.getValue();
                    if (!verifyResult.isPass()) {
                        stringBuilder.append(index);
                        stringBuilder.append(":");
                        stringBuilder.append(verifyResult.getErrMessage()).append(" ");
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

        if (stringBuilder.length() > 0) {
            stringBuilder.insert(0, " element verify: ");
        }

        VerifyInfo verifyInfo = afterConvert == null ? null : afterConvert.validate(newArray);
        if (verifyInfo != null && !verifyInfo.isPass()) {
            stringBuilder.insert(0, " array verify: " + verifyInfo.getErrMessage() + ". ");
        }

        if (stringBuilder.length() > 0) {
            return new VerifyResult<>(newArray, stringBuilder.insert(0,"{").append("}").toString());
        } else {
            return new VerifyResult<>(newArray);
        }
    }

    @Override
    public boolean supports(Object value) {
        return value != null && value.getClass().isArray();
    }
}
