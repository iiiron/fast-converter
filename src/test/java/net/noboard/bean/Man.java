//package net.noboard.bean;
//
//import lombok.Data;
//import net.noboard.fastconverter.ConverterIndicator;
//import net.noboard.fastconverter.handler.*;
//import net.noboard.fastconverter.handler.base.SkippingConverterHandler;
//
//import java.util.*;
//
//@Data
//public class Man implements Comparable {
//
//    private String name;
//
//    @ConverterIndicator(converter = DateToFormatStringConverterHandler.class)
//    private Date birthday;
//
//    @ConverterIndicator(converter = SkippingConverterHandler.class, afterConvert = AgeVarify.class)
//    private Integer age;
//
//    @ConverterIndicator(converter = BooleanToStringConverterHandler.class)
//    private Boolean sex;
//
//    @ConverterIndicator(converter = SkippingConverterHandler.class, afterConvert = AgeVarify2.class)
//    private Integer age2;
//
//    private int[] aaa;
//
//    private Integer[] bbb;
//
//    private Set ccc;
//
//    private List<String>[] ddd;
//
//    private Map<String, Date> eee;
//
//    @ConverterIndicator(converter = BooleanToNumberConverterHandler.class)
//    @ConverterIndicator(converter = NumberToBigDecimalConverterHandler.class)
//    private Boolean fff;
//
//    @Override
//    public int compareTo(Object o) {
//        return this.getName().compareTo(((Man)o).getName());
//    }
//}
