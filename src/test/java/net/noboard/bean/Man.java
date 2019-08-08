package net.noboard.bean;

import lombok.Data;
import net.noboard.fastconverter.ConvertField;
import net.noboard.fastconverter.Convertible;
import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.handler.*;

import java.util.*;

@Data
@Convertible
public class Man implements Comparable {

    private String name;

    @ConvertField(converter = DateToFormatStringConverterHandler.class)
    private Date birthday;

    private Integer age;

    @FieldConverter(converter = BooleanToStringConverterHandler.class)
    private Boolean sex;


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
