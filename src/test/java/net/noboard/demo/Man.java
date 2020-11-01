package net.noboard.demo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleBeanType;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.NumberToBigDecimalConverterHandler;

import java.util.Date;

@Getter
@Setter
@ToString
@ConvertibleBean(type = ConvertibleBeanType.SOURCE, targetClass = Woman.class)
public class Man {

    @DateFormat
    private Date birthday;

    @ConvertibleField(converter = NumberToBigDecimalConverterHandler.class)
    @ConvertibleField(converter = NumberToBigDecimalConverterHandler.class, group = "a")
    private Integer age;

    @ToBig
    private Boolean sex;

    private String name;
}
