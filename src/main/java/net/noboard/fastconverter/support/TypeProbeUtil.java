package net.noboard.fastconverter.support;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

/**
 * 类型侦测工具
 *
 * 如果是容器，它会侦测容器中元素的类型，如果是元素则直接返回元素类型
 *
 * @date 2020/10/31 4:19 下午
 * @author by wanxm
 */
public class TypeProbeUtil {
    public static Class<?> find(Field field) {
        // todo wanxm array，Map的感知
        if (Collection.class.isAssignableFrom(field.getType())) {
            return getGenericClass(field);
        } else {
            return field.getType();
        }
    }

    public static Class<?> getGenericClass(Field field) {
        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        return (Class<?>) stringListType.getActualTypeArguments()[0];
    }
}
