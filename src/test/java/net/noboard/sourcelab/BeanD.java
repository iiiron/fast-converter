package net.noboard.sourcelab;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.NumberToStringConverterHandler;

@Data
@ConvertibleBean(targetClass = BeanC.class)
@ConvertibleBean(targetClass = BeanC.class, group = "b")
public class BeanD {
    @ConvertibleField(converter = NumberToStringConverterHandler.class)
    @ConvertibleField(converter = StringAddPrefixConverter.class, tip = "默认分租-")
    @ConvertibleField(converter = NumberToStringConverterHandler.class, group = "b")
    @ConvertibleField(converter = StringAddPrefixConverter.class, group = "b",tip = "b分租-")
    private String age;
}
