package net.noboard.sourcelab;

import lombok.Data;
import lombok.ToString;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.GroupSelector;
import net.noboard.fastconverter.handler.NumberToStringConverterHandler;

import java.util.List;

@Data
@ToString
@ConvertibleBean(targetClass = BeanA.class)
@ConvertibleBean(targetClass = BeanA.class, group = "a")
public class BeanB {

    @ConvertibleField(converter = NumberToStringConverterHandler.class)
    @ConvertibleField(converter = NumberToStringConverterHandler.class, group = "a")
    @ConvertibleField(converter = StringAddPrefixConverter.class, group = "a", tip = "abc")
    private String age;

    @GroupSelector(group = "a", beanGroup = "b")
    private List<BeanD> name;

    private BeanD tag;

    @ConvertibleField(group = "a", nameTo = "stringList")
    private List<String> b;
}
