package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.core.AbstractConverterHandler;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 越过转换器
 * <p>
 * 该转换器不对数据进行任何转换，它的存在是为了让过滤器转换器提供对某些类型的数据的跳过逻辑。
 */
public class SkippingConverterHandler extends AbstractConverterHandler<Object, Object> {

    private Class[] classes;

    /**
     * 8中基本数据类型的包装类 + String
     */
    public static Class[] BASIC_DATA_TYPE = {Integer.class, Double.class, Float.class,
            Long.class, Byte.class, Short.class, Character.class, String.class, Boolean.class};

    /**
     * 常见数据类型
     */
    public static Class[] COMMON_DATA_TYPE = {Date.class, BigDecimal.class};

    public SkippingConverterHandler(Class... classes) {
        this.classes = classes;
    }

    public SkippingConverterHandler() {

    }

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        return value;
    }

    @Override
    public boolean supports(Object value) {
        if (classes == null || Array.getLength(classes) == 0) {
            return true;
        } else {
            if (value == null) {
                return false;
            }
            for (Class clazz : classes) {
                if (clazz.isAssignableFrom(value.getClass())) {
                    return true;
                }
            }
            return false;
        }
    }
}
