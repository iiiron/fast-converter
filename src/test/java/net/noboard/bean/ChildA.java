//package net.noboard.bean;
//
//import lombok.Data;
//import net.noboard.fastconverter.BeanConverterIndicator;
//import net.noboard.fastconverter.ConverterIndicator;
//import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;
//
//import java.util.Date;
//
//@Data
////@BeanConverterIndicator(targetType = ChildB.class, afterConvert = BeanAgeVarify.class)
//public class ChildA {
//    private String name;
//
//    @ConverterIndicator(converter = DateToFormatStringConverterHandler.class)
//    private Date birthday;
//
//    private Integer age;
//}
