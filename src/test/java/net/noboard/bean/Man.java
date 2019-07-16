package net.noboard.bean;

import lombok.Data;
import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.handler.*;
import net.noboard.fastconverter.handler.base.SkippingConverterHandler;

import java.util.*;

@Data
public class Man implements Comparable {

    private String name;

    @FieldConverter(converter = DateToFormatStringConverterHandler.class)
    private Date birthday;

    @FieldConverter(converter = SkippingConverterHandler.class, afterConvert = AgeVarify.class)
    private Integer age;

    @FieldConverter(converter = BooleanToStringConverterHandler.class)
    private Boolean sex;

    @FieldConverter(converter = SkippingConverterHandler.class, afterConvert = AgeVarify2.class)
    private Integer age2;

    private int[] aaa;

    private Integer[] bbb;

    private Set ccc;

    private List<String>[] ddd;

    private Map<String, Date> eee;

    @FieldConverter(converter = BooleanToNumberConverterHandler.class)
    @FieldConverter(converter = NumberToBigDecimalConverterHandler.class)
    private Boolean fff;

    @Override
    public int compareTo(Object o) {
        return this.getName().compareTo(((Man)o).getName());
    }
}
