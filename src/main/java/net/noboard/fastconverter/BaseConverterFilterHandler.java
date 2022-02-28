package net.noboard.fastconverter;

import net.noboard.fastconverter.handler.*;
import net.noboard.fastconverter.handler.bean.BeanConverterHandler;
import net.noboard.fastconverter.handler.container.CollectionToCollectionConverterHandler;
import net.noboard.fastconverter.handler.container.MapToMapConverterHandler;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseConverterFilterHandler implements ConverterFilter {

    private final static BooleanToStringConverterHandler booleanToStringConverterHandler = new BooleanToStringConverterHandler();
    private final static DateToFormatStringConverterHandler dateToFormatStringConverterHandler = new DateToFormatStringConverterHandler();
    private final static DateToTimeStampConverterHandler dateToTimeStampConverterHandler = new DateToTimeStampConverterHandler();
    private final static EnumToNameConverterHandler enumToNameConverterHandler = new EnumToNameConverterHandler();
    private final static NameToEnumConverterHandler nameToEnumConverterHandler = new NameToEnumConverterHandler();
    private final static NumberToBigDecimalConverterHandler numberToBigDecimalConverterHandler = new NumberToBigDecimalConverterHandler();
    private final static NumberToStringConverterHandler numberToStringConverterHandler = new NumberToStringConverterHandler();

    private final static CollectionToCollectionConverterHandler collectionToCollectionConverterHandler = new CollectionToCollectionConverterHandler();
    private final static MapToMapConverterHandler mapToMapConverterHandler = new MapToMapConverterHandler();
    private final static BeanConverterHandler beanConverterHandler = new BeanConverterHandler();

    private static ConverterMap converterMap = new ConverterMap();

    static {
        converterMap.put(Boolean.class, String.class, booleanToStringConverterHandler);
        converterMap.put(Date.class, String.class, dateToFormatStringConverterHandler);
        converterMap.put(Date.class, Long.class, dateToTimeStampConverterHandler);
        converterMap.put(Enum.class, String.class, enumToNameConverterHandler);
        converterMap.put(String.class, Enum.class, nameToEnumConverterHandler);

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
        Class sourceClass = ParameterizedType.class.isAssignableFrom(convertInfo.getSourceType().getClass()) ? (Class) ((ParameterizedType) convertInfo.getSourceType()).getRawType() : (Class) convertInfo.getSourceType();
        Class targetClass = ParameterizedType.class.isAssignableFrom(convertInfo.getTargetType().getClass()) ? (Class) ((ParameterizedType) convertInfo.getTargetType()).getRawType() : (Class) convertInfo.getTargetType();

        ConvertInfo currentConvertInfo = new ConvertInfo();
        currentConvertInfo.setModeType(convertInfo.getModeType());
        currentConvertInfo.setConverterFilter(convertInfo.getConverterFilter());
        currentConvertInfo.setGroup(convertInfo.getGroup());
        currentConvertInfo.setSourceType(convertInfo.getSourceType());
        currentConvertInfo.setTargetType(convertInfo.getTargetType());

        // 优先检查是否是被注解标注的实体, 是的话走bean转换器
        if ((convertInfo.getModeType() == ConvertibleBeanType.SOURCE && ConvertibleAnnotatedUtils.getMergedConvertBean(sourceClass, convertInfo.getGroup()) != null)
                || (convertInfo.getModeType() == ConvertibleBeanType.TARGET && ConvertibleAnnotatedUtils.getMergedConvertBean(targetClass, convertInfo.getGroup()) != null)) {
            return () -> {
                return beanConverterHandler.convert(value, currentConvertInfo);
            };
        }

        // 其次检查是否是容器
        if (Collection.class.isAssignableFrom(sourceClass)) {
            return () -> {
                return collectionToCollectionConverterHandler.convert(value, currentConvertInfo);
            };
        } else if (Map.class.isAssignableFrom(sourceClass)) {
            return () -> {
                return mapToMapConverterHandler.convert(value, currentConvertInfo);
            };
        }

        // 最后走默认换
        Converter converter = converterMap.get(sourceClass, targetClass);
        if (converter != null) {
            return () -> {
                return converter.convert(value);
            };
        }

        return () -> value;
    }
}
