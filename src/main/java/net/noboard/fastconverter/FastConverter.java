package net.noboard.fastconverter;

import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

public class FastConverter {

    private static final BaseConverterFilterHandler baseConvertFilterHandler = new BaseConverterFilterHandler();

    public static <T> T autoConvert(Object bean, String group) {
        if (bean == null) {
            return null;
        }
        return autoConvert(bean, null, group);
    }

    public static <T> T autoConvert(Object bean) {
        return autoConvert(bean, null, Converter.DEFAULT_GROUP);
    }

    public static <T> T autoConvert(Object source, Class<T> target) {
        return autoConvert(source, target, Converter.DEFAULT_GROUP);
    }

    public static <T> T autoConvert(Object source, Class<T> target, String group) {
        if (source == null) {
            return null;
        }

        ConvertInfo convertInfo = new ConvertInfo();
        convertInfo.setConverterFilter(baseConvertFilterHandler);
        convertInfo.setGroup(group);
        convertInfo.setSourceType(source.getClass());

        if (target == null) {
            convertInfo.setModeType(ConvertibleBeanType.SOURCE);
            ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(source.getClass(), group);
            if (convertibleBean != null && ConvertibleBeanType.SOURCE.equals(convertibleBean.type())) {
                convertInfo.setTargetType(ConvertibleAnnotatedUtils.getRelevantClass(convertibleBean));
            } else {
                throw new ConvertException(String.format("the source bean %s defect @ConvertibleBean with type of ConvertibleBeanType.SOURCE", source.getClass().getName()));
            }
        } else {
            convertInfo.setModeType(ConvertibleBeanType.TARGET);
            convertInfo.setTargetType(target);
        }

        return (T) baseConvertFilterHandler.filter(source, convertInfo).convert();
    }
}
