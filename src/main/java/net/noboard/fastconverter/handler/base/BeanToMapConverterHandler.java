package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;

/**
 * 用以将对象转换为map。
 * <p>
 * 通过@FieldConverter指定了转换器的域为null，则对应key不会出现在map中。
 * 没有转换器支持的域将以原值推入map中。
 *
 * @author wanxm
 * @return
 */
public class BeanToMapConverterHandler extends AbstractBeanConverterHandler<Object, Map<String, Object>> {

    public BeanToMapConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    @Override
    protected Map<String, Object> converting(Object value, String tip) throws ConvertException {
        return this.convertingAndVerify(value, tip, null).getValue();
    }

    @Override
    protected VerifyResult<Map<String, Object>> convertingAndVerify(Object value, String tip, Validator afterConvert) throws ConvertException {
        Map<String, Object> map = new HashMap<>();
        Map<String, VerifyInfo> errMap = new HashMap<>();

        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(value.getClass());
        } catch (IntrospectionException e) {
            throw new ConvertException("获取" + value.getClass() + "的bean info时出错。", e);
        }

        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            if ("class".equals(descriptor.getName())) {
                continue;
            }

            // 获取域值
            Object fieldValue;
            try {
                fieldValue = descriptor.getReadMethod().invoke(value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ConvertException(e);
            }

            // 对域名 及 是否隐藏的处理
            String mapKey;
            Field docField;
            java.lang.reflect.Field field;
            try {
                field = value.getClass().getDeclaredField(descriptor.getName());
                docField = field.getAnnotation(Field.class);
            } catch (NoSuchFieldException e) {
                throw new ConvertException(
                        MessageFormat.format("BeanToMapConverterHandler反射获取对象域时发生异常：NoSuchFieldException  {0}.{1}  通常是因为在转换器过滤器中没有添加对相关类的支持，导致某些非Bean的java类被错误的抛给了BeanToMapConverterHandler去处理", value.getClass().getName(), descriptor.getName()),
                        e);
            }
            if (docField != null) {
                if (docField.isHide()) {
                    continue;
                }

                mapKey = "".equals(docField.name())
                        ? descriptor.getName()
                        : docField.name();
            } else {
                mapKey = descriptor.getName();
            }

            // 转换域值
            Object mapValue;
            ConverterIndicator[] annotations;
            annotations = field.getAnnotationsByType(ConverterIndicator.class);
            if (annotations == null || annotations.length == 0) {
                Converter converter = this.filter(fieldValue);
                // 有转换器取转换后的值，没有转换器取原值
                if (converter == null) {
                    mapValue = fieldValue;
                } else if (AbstractBeanConverterHandler.class.isAssignableFrom(converter.getClass())) {
                    VerifyResult verifyResult = converter.convertAndVerify(fieldValue);
                    mapValue = verifyResult.getValue();
                    if (!verifyResult.isPass()) {
                        errMap.put(mapKey, verifyResult);
                    }
                } else {
                    mapValue = converter.convert(fieldValue);
                }
            } else {
                // 如果该域指定了转换器，域值为空，则跳过该域
                if (fieldValue == null) {
                    continue;
                }
                Object v = fieldValue;
                for (ConverterIndicator annotation : annotations) {
                    VerifyResult verifyResult = this.handlerAndVerify(annotation, v);
                    v = verifyResult.getValue();
                    if (!verifyResult.isPass()) {
                        errMap.put(field.getName(), verifyResult);
                    }
                }
                mapValue = v;
            }

            map.put(mapKey, mapValue);
        }

        VerifyInfo verifyInfo = null;
        if (afterConvert == null) {
            BeanConverterIndicator converterIndicator = value.getClass().getAnnotation(BeanConverterIndicator.class);
            if (converterIndicator != null && !converterIndicator.afterConvert().isInterface()) {
                try {
                    afterConvert = converterIndicator.afterConvert().newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new ConvertException("实例化验证器" + converterIndicator.afterConvert() + "失败");
                }
            }
        }
        if (afterConvert != null) {
            verifyInfo = afterConvert.validate(map);
        }

        if ((verifyInfo != null && !verifyInfo.isPass()) || errMap != null) {
            return new VerifyResult<>(map, MessageFormat.format("整体校验结果：{0}。对域的校验结果：{1}",
                    (verifyInfo == null || verifyInfo.getErrMessage() == null) ? "success" : verifyInfo.getErrMessage(),
                    errMap == null ? "success" : errMap.toString()));
        } else {
            return new VerifyResult<>(map);
        }
    }

    @Override
    public boolean supports(Object value) {
        return value != null;
    }
}
