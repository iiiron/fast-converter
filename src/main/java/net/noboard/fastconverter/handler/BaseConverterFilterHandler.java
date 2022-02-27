package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConverteModeType;
import net.noboard.fastconverter.ConverterFacade;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.handler.bean.BeanConverterHandler;
import net.noboard.fastconverter.handler.bean.ConvertInfo;
import net.noboard.fastconverter.handler.container.ArrayToArrayConverterHandler;
import net.noboard.fastconverter.handler.container.CollectionToCollectionConverterHandler;
import net.noboard.fastconverter.handler.container.MapToMapConverterHandler;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;

public class BaseConverterFilterHandler implements ConverterFilter {

    private BooleanToStringConverterHandler booleanToStringConverterHandler = new BooleanToStringConverterHandler();
    private DateToFormatStringConverterHandler dateToFormatStringConverterHandler = new DateToFormatStringConverterHandler();
    private DateToTimeStampConverterHandler dateToTimeStampConverterHandler = new DateToTimeStampConverterHandler();
    private EnumToNameConverterHandler enumToNameConverterHandler = new EnumToNameConverterHandler();
    private NameToEnumConverterHandler nameToEnumConverterHandler = new NameToEnumConverterHandler();
    private NumberToBigDecimalConverterHandler numberToBigDecimalConverterHandler = new NumberToBigDecimalConverterHandler();
    private NumberToStringConverterHandler numberToStringConverterHandler = new NumberToStringConverterHandler();
    private SkipConverterHandler skipConverterHandler = new SkipConverterHandler();

    private ArrayToArrayConverterHandler arrayToArrayConverterHandler = new ArrayToArrayConverterHandler();
    private CollectionToCollectionConverterHandler collectionToCollectionConverterHandler = new CollectionToCollectionConverterHandler();
    private MapToMapConverterHandler mapToMapConverterHandler = new MapToMapConverterHandler();
    private BeanConverterHandler beanConverterHandler = new BeanConverterHandler();

    public BaseConverterFilterHandler() {

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
        if ((convertInfo.getModeType() == ConverteModeType.SOURCE && ConvertibleAnnotatedUtils.getMergedConvertBean(sourceClass, convertInfo.getGroup()) != null)
                || (convertInfo.getModeType() == ConverteModeType.TARGET && ConvertibleAnnotatedUtils.getMergedConvertBean(targetClass, convertInfo.getGroup()) != null)) {
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
        if (Boolean.class.isAssignableFrom(sourceClass)) {
            if (String.class.isAssignableFrom(targetClass)) {
                return () -> {
                    return booleanToStringConverterHandler.convert((Boolean) value);
                };
            }
        } else if (sourceClass == targetClass) {
            return () -> value;
        }

        return () -> value;
    }
}
