package net.noboard.fastconverter.handler.support;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.FieldConverter;

import java.util.ArrayList;
import java.util.List;

public class FieldConverterHandler {

    //TODO 找一个线程安全的list实现
    private static List<Converter> converters = new ArrayList<>();

    public Object handler(FieldConverter annotation, Object value) {
        Converter cc = null;
        for (Converter converter : converters) {
            if (converter.getClass().equals(annotation.converter())) {
                cc = converter;
                break;
            }
        }
        if (cc == null) {
            try {
                cc = annotation.converter().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ConvertException("创建" + annotation.converter().getName() + "实例失败：", e);
            }
            converters.add(cc);
        }

        if ("".equals(annotation.tip())) {
            return cc.convert(value);
        } else {
            return cc.convert(value, annotation.tip());
        }
    }
}
