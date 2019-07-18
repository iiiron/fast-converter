package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.handler.support.ConverterExceptionHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Collection转换器
 * <p>
 * 该转换器将根据原Collection的实际类型生成一个新的相同类型的实例。并使用ConverterFilter中注册的转换器对
 * 原容器中的元素一一进行转换，转换结果推入新容器中。如果ConverterFilter没有筛选出转换器，则推入原数据。
 */
public class CollectionToCollectionConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<Collection<T>, Collection<K>> {

    public CollectionToCollectionConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected Collection<K> converting(Collection<T> value, String tip) throws ConvertException {
        return this.convertingAndVerify(value, tip, null).getValue();
    }

    @Override
    protected VerifyResult<Collection<K>> convertingAndVerify(Collection<T> value, String tip, Validator afterConvert) throws ConvertException {
        Collection<K> collection;
        try {
            collection = value.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ConvertException("实例化" + value.getClass() + "失败", e);

        }

        Converter converter = null;
        Object newV = null, oldV = null;
        Map<String, VerifyInfo> errMap = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            int index = 0;
            for (T obj : value) {
                converter = this.filter(obj);
                oldV = obj;
                if (converter == null) {
                    newV = oldV;
                } else {
                    VerifyResult verifyResult = converter.convertAndVerify(oldV, tip);
                    newV = verifyResult.getValue();
                    if (!verifyResult.isPass()) {
                        stringBuilder.append(index);
                        stringBuilder.append(":");
                        stringBuilder.append(verifyResult.getErrMessage()).append(" ");
                    }
                }
                collection.add((K) newV);
                index++;
            }
        } catch (Exception e) {
            ConverterExceptionHelper.factory(value, oldV, collection, newV, converter, e);
        }

        if (stringBuilder.length() > 0) {
            stringBuilder.insert(0, " element verify: ");
        }

        VerifyInfo verifyInfo = afterConvert == null ? null : afterConvert.validate(collection);
        if (verifyInfo != null && !verifyInfo.isPass()) {
            stringBuilder.insert(0, " collection verify: " + verifyInfo.getErrMessage() + ". ");
        }

        if (stringBuilder.length() > 0) {
            return new VerifyResult<>(collection, stringBuilder.insert(0,"{").append("}").toString());
        } else {
            return new VerifyResult<>(collection);
        }
    }

    @Override
    public boolean supports(Object value) {
        return value != null && Collection.class.isAssignableFrom(value.getClass());
    }
}
