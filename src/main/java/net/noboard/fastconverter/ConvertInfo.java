package net.noboard.fastconverter;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBeanType;
import net.noboard.fastconverter.ConverterFilter;

import java.lang.reflect.Type;

@Data
public class ConvertInfo {

    /**
     * 转换模式, 1=source模式, 2=target模式
     */
    private ConvertibleBeanType modeType;

    private ConverterFilter converterFilter;

    private String group;

    /**
     * 持有数据的实体的Type
     */
    private Type sourceType;

    /**
     * 转换后的实体的Type
     */
    private Type targetType;
}
