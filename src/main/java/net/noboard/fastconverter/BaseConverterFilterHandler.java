package net.noboard.fastconverter;

import net.noboard.fastconverter.handler.*;
import net.noboard.fastconverter.handler.bean.BeanConverterHandler;
import net.noboard.fastconverter.handler.container.CollectionToCollectionConverterHandler;
import net.noboard.fastconverter.handler.container.MapToMapConverterHandler;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseConverterFilterHandler implements ConverterFilter {

    private static final BooleanToStringConverterHandler booleanToStringConverterHandler = new BooleanToStringConverterHandler();
    private static final BooleanToIntegerConverterHandler booleanToIntegerConverterHandler = new BooleanToIntegerConverterHandler();
    private static final StringToBooleanConverterHandler stringToBooleanConverterHandler = new StringToBooleanConverterHandler();
    private static final StringToIntegerConverterHandler stringToIntegerConverterHandler = new StringToIntegerConverterHandler();
    private static final StringToLongConverterHandler stringToLongConverterHandler = new StringToLongConverterHandler();
    private static final StringToDoubleConverterHandler stringToDoubleConverterHandler = new StringToDoubleConverterHandler();
    private static final FormatStringToDateConverterHandler formatStringToDateConverterHandler = new FormatStringToDateConverterHandler();
    private static final DateToFormatStringConverterHandler dateToFormatStringConverterHandler = new DateToFormatStringConverterHandler();
    private static final DateToLongConverterHandler dateToLongConverterHandler = new DateToLongConverterHandler();
    private static final LongToDateConverterHandler longToDateConverterHandler = new LongToDateConverterHandler();
    private static final EnumToNameConverterHandler enumToNameConverterHandler = new EnumToNameConverterHandler();
    private static final NameToEnumConverterHandler nameToEnumConverterHandler = new NameToEnumConverterHandler();
    private static final NumberToBigDecimalConverterHandler numberToBigDecimalConverterHandler = new NumberToBigDecimalConverterHandler();
    private static final NumberToStringConverterHandler numberToStringConverterHandler = new NumberToStringConverterHandler();

    private static final CollectionToCollectionConverterHandler collectionToCollectionConverterHandler = new CollectionToCollectionConverterHandler();
    private static final MapToMapConverterHandler mapToMapConverterHandler = new MapToMapConverterHandler();
    private static final BeanConverterHandler beanConverterHandler = new BeanConverterHandler();

    private static ConverterMap converterMap = new ConverterMap();

    static {
        converterMap.put(Boolean.class, String.class, booleanToStringConverterHandler);
        converterMap.put(Boolean.class, Integer.class, booleanToIntegerConverterHandler);
        converterMap.put(String.class, Boolean.class, stringToBooleanConverterHandler);
        converterMap.put(String.class, Enum.class, nameToEnumConverterHandler);
        converterMap.put(String.class, Date.class, formatStringToDateConverterHandler);
        converterMap.put(String.class, Integer.class, stringToIntegerConverterHandler);
        converterMap.put(String.class, Long.class, stringToLongConverterHandler);
        converterMap.put(String.class, Double.class, stringToDoubleConverterHandler);
        converterMap.put(Date.class, String.class, dateToFormatStringConverterHandler);
        converterMap.put(Date.class, Long.class, dateToLongConverterHandler);
        converterMap.put(Enum.class, String.class, enumToNameConverterHandler);
        converterMap.put(Long.class, Date.class, longToDateConverterHandler);

        converterMap.put(Integer.class, BigDecimal.class, numberToBigDecimalConverterHandler);
        converterMap.put(Double.class, BigDecimal.class, numberToBigDecimalConverterHandler);
        converterMap.put(Float.class, BigDecimal.class, numberToBigDecimalConverterHandler);
        converterMap.put(Long.class, BigDecimal.class, numberToBigDecimalConverterHandler);
        converterMap.put(Short.class, BigDecimal.class, numberToBigDecimalConverterHandler);
        converterMap.put(Byte.class, BigDecimal.class, numberToBigDecimalConverterHandler);

        converterMap.put(Integer.class, String.class, numberToStringConverterHandler);
        converterMap.put(Double.class, String.class, numberToStringConverterHandler);
        converterMap.put(Float.class, String.class, numberToStringConverterHandler);
        converterMap.put(Long.class, String.class, numberToStringConverterHandler);
        converterMap.put(Short.class, String.class, numberToStringConverterHandler);
        converterMap.put(Byte.class, String.class, numberToStringConverterHandler);
    }

    private static class ConverterMap {
        private static final Map<Class, Map<Class, Converter>> converterMap = new HashMap<>();

        public void put(Class sourceClass, Class targetClass, Converter converter) {
            Map<Class, Converter> converterMap = ConverterMap.converterMap.computeIfAbsent(sourceClass, (t) -> new HashMap<>());
            converterMap.put(targetClass, converter);
        }

        public Converter get(Class sourceClass, Class targetClass) {
            for (Map.Entry<Class, Map<Class, Converter>> classMapEntry : converterMap.entrySet()) {
                if (classMapEntry.getKey().isAssignableFrom(sourceClass)) {
                    for (Map.Entry<Class, Converter> classConverterEntry : classMapEntry.getValue().entrySet()) {
                        if (classConverterEntry.getKey().isAssignableFrom(targetClass)) {
                            return classConverterEntry.getValue();
                        }
                    }
                }
            }
            return null;
        }
    }

    @Override
    public ConverterFacade filter(Object value, ConvertInfo convertInfo) {
        Class sourceClass = parse(convertInfo.getSourceType());
        Class targetClass = parse(convertInfo.getTargetType());

        // if 优先检查是否是被注解标注的实体, 并且是source转换, 如果是, 直接取实体上标注的target类型
        if (convertInfo.getModeType() == ConvertibleBeanType.SOURCE && value != null) {
            ConvertibleBean mergedConvertBean = ConvertibleAnnotatedUtils.getMergedConvertBean(value.getClass(), convertInfo.getGroup());
            if (mergedConvertBean != null) {
                convertInfo.setSourceType(value.getClass());
                convertInfo.setTargetType(ConvertibleAnnotatedUtils.getRelevantClass(mergedConvertBean));
                return () -> {
                    return beanConverterHandler.convert(value, convertInfo);
                };
            }
        }

        // if 优先检查是否是被注解标注的实体, 是的话走bean转换器
        if ((convertInfo.getModeType() == ConvertibleBeanType.SOURCE && ConvertibleAnnotatedUtils.getMergedConvertBean(sourceClass, convertInfo.getGroup()) != null)
                || (convertInfo.getModeType() == ConvertibleBeanType.TARGET && ConvertibleAnnotatedUtils.getMergedConvertBean(targetClass, convertInfo.getGroup()) != null)) {
            return () -> {
                return beanConverterHandler.convert(value, convertInfo);
            };
        }

        // 其次检查是否是容器
        if (Collection.class.isAssignableFrom(sourceClass)) {
            return () -> {
                return collectionToCollectionConverterHandler.convert(value, convertInfo);
            };
        } else if (Map.class.isAssignableFrom(sourceClass)) {
            return () -> {
                return mapToMapConverterHandler.convert(value, convertInfo);
            };
        }

        // 最后走默认换
        Converter converter = converterMap.get(sourceClass, targetClass);
        if (converter != null) {
            return () -> {
                if (converter == nameToEnumConverterHandler) {
                    return converter.convert(value, convertInfo.getTargetType());
                }

                return converter.convert(value);
            };
        }

        return () -> value;
    }

    private Class parse(Type type) {
        if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
            return (Class) ((ParameterizedType) type).getRawType();
        }
        if (WildcardType.class.isAssignableFrom(type.getClass())) {
            return (Class) ((WildcardType) type).getUpperBounds()[0];
        }

        return (Class) type;
    }
}
