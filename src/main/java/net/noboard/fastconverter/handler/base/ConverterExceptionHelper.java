package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;

import java.text.MessageFormat;

public class ConverterExceptionHelper {
    public static void factory(Object oldContainer, Object oldValue, Object newContainer, Object newValue, Converter converter, Throwable e) throws ConvertException {
        throw new ConvertException(
                MessageFormat.format(
                        "旧容器类型：{0}，旧元素类型：{1}，新容器类型：{2}，新元素类型：{3}，转换器类型：{4}，",
                        oldContainer ==null ? "null" : oldContainer.getClass().getName(),
                        oldValue == null ? "null" : oldValue.getClass().getName(),
                        newContainer == null ? "null" : newContainer.getClass().getName(),
                        newValue == null ? "null" : newValue.getClass().getName(),
                        converter == null ? "null" : converter.getClass().getName()),
                e);
    }
}
