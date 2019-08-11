package net.noboard.bean;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.*;

import java.util.*;

@Data
@ConvertibleBean(targetClass = Woman.class)
@ConvertibleBean(targetClass = Woman.class, group = "b")
//@ConvertibleBean(targetClass = ChildA.class, group = "B")
public class Man {
    private String name = "wanxm";

    @ConvertibleField(group = "b", tip = "toChildC")
    List<List<ChildA>> child;

    @ConvertibleField(group = "b",converter = BooleanToNumberConverterHandler.class)
    @ConvertibleField(group = "b",converter = NumberToBigDecimalConverterHandler.class)
    @ConvertibleField(converter = BooleanToNumberConverterHandler.class)
    @ConvertibleField(converter = NumberToBigDecimalConverterHandler.class)
    private boolean sex;

    private Date birthday;

    private Integer age;

    private Integer[] bbb;

    private Boolean fff;

    private Set<String> ccc;

    private List<String>[] ddd;

    private Map<String, Date> eee;
}
