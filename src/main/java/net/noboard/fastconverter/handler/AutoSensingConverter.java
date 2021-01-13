package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.core.AbstractConverterHandler;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 自动感知转换器
 *
 * 通过识别数据和转换结果的类型，来自动执行一个转换过程
 *
 * @date 2020/11/11 9:22 下午
 * @author by wanxm
 */
public class AutoSensingConverter extends AbstractConverterHandler<Object, Object> {

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        Class target;
        try {
            target = Class.forName(tip);
        } catch (ClassNotFoundException e) {
            return null;
        }

        if (value instanceof String) {
            // 源是字符串，目标是枚举，直接value of
            if (target.isEnum()) {
                if (StringUtils.isEmpty(value)) {
                    return null;
                }
                return Enum.valueOf(target, (String) value);
            }
        } else if (value instanceof Enum) {
            // 源是枚举，目标是字符串，直接取name
            if (target.equals(String.class)) {
                return ((Enum)value).name();
            }
        }

        return null;
    }

    @Override
    public boolean supports(Object value, String tip) {
        Class target;
        try {
            target = Class.forName(tip);
        } catch (ClassNotFoundException e) {
            return false;
        }

        if (value instanceof String && target.isEnum()) {
            return true;
        } else if (value instanceof Enum && target.equals(String.class)) {
            return true;
        } else {
            return false;
        }
    }
}
