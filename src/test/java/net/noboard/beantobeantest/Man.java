package net.noboard.beantobeantest;

import lombok.Data;
import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.handler.BooleanToStringConverterHandler;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class Man {

    private String name;

    @FieldConverter(converter = DateToFormatStringConverterHandler.class)
    private Date birthday;

    private Integer age;

    @FieldConverter(converter = BooleanToStringConverterHandler.class)
    private Boolean sex;


    private int[] aaa;

    private Integer[] bbb;

    private Set ccc;

    private List<String>[] ddd;

    private Map<String, Date> eee;
}
