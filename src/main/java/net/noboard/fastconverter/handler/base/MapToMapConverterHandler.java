//package net.noboard.fastconverter.handler.base;
//
//import net.noboard.fastconverter.*;
//import net.noboard.fastconverter.handler.support.ConverterExceptionHelper;
//
//import java.util.Map;
//
//
///**
// * Map转换器
// * <p>
// * 该转换器将根据原Map的实际类型生成一个新的相同类型的实例。并使用ConverterFilter中注册的转换器对
// * 原Map中的元素的value进行转换，将转换结果推入新的Map中（key不变，value为转换后的值）。如果转换器
// * 筛选器没有筛选出针对某一值的转换器，则将此原值推入新的Map中。
// */
//public class MapToMapConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<Map<Object, T>, Map<Object, K>> {
//
//    public MapToMapConverterHandler(ConverterFilter converterFilter) {
//        super(converterFilter);
//    }
//
//    @Override
//    public boolean supports(Object value) {
//        return value != null && Map.class.isAssignableFrom(value.getClass());
//    }
//
//    @Override
//    protected Map<Object, K> converting(Map<Object, T> value, String tip) throws ConvertException {
//        return this.convertingAndVerify(value, tip, null).getValue();
//    }
//
//    @Override
//    protected ConvertResult<Map<Object, K>> convertingAndVerify(Map<Object, T> value, String tip, Validator afterConvert) throws ConvertException {
//        Map<Object, K> newMap;
//        try {
//            newMap = value.getClass().newInstance();
//        } catch (InstantiationException | IllegalAccessException e) {
//            throw new ConvertException("实例化" + value.getClass() + "失败", e);
//        }
//
//        Converter converter = null;
//        Object newV = null, oldV = null;
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            for (Map.Entry entry : value.entrySet()) {
//                oldV = entry.getValue();
//                converter = this.getConverter(oldV);
//                if (converter != null) {
//                    ConvertResult verifyResult = converter.convertAndVerify(oldV, tip);
//                    newV = verifyResult.getValue();
//                    if (!verifyResult.isPass()) {
//                        stringBuilder.append(entry.getKey());
//                        stringBuilder.append(":");
//                        stringBuilder.append(verifyResult.getErrMessage()).append(" ");
//                    }
//                } else {
//                    newV = oldV;
//                }
//                newMap.put(entry.getKey(), (K) newV);
//            }
//        } catch (Exception e) {
//            ConverterExceptionHelper.factory(value, oldV, newMap, newV, converter, e);
//        }
//
//        if (stringBuilder.length() > 0) {
//            stringBuilder.insert(0, " element verify: ");
//        }
//
//        VerifyInfo verifyInfo = afterConvert == null ? null : afterConvert.validate(newMap);
//        if (verifyInfo != null && !verifyInfo.isPass()) {
//            stringBuilder.insert(0, " map verify: " + verifyInfo.getErrMessage());
//        }
//
//        if (stringBuilder.length() > 0) {
//            return new ConvertResult<>(newMap, stringBuilder.insert(0,"{").append("}").toString());
//        } else {
//            return new ConvertResult<>(newMap);
//        }
//    }
//}
