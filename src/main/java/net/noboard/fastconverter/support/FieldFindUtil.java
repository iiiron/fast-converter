package net.noboard.fastconverter.support;

import java.lang.reflect.Field;

public class FieldFindUtil {
    public static Field find(Class<?> clazz, String fieldName) {
        while (true) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {

            }
            clazz = clazz.getSuperclass();
            if (clazz == Object.class) {
                return null;
            }
        }
    }
}
