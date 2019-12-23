package net.noboard.demo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.BooleanToStringConverterHandler;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;
import net.noboard.fastconverter.handler.NumberToBigDecimalConverterHandler;

import java.util.Date;

@ConvertibleBean(targetClass = Woman.class)
@Getter
@Setter
@ToString
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
