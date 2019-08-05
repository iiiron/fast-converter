//package net.noboard.fastconverter;
//
//import java.lang.annotation.*;
//
///**
// * @author wanxm
// */
//@Retention(RetentionPolicy.RUNTIME)
//@Target({ElementType.FIELD})
//@Repeatable(ConverterIndicators.class)
//public @interface ConverterIndicator {
//    String tip() default "";
//
//    Class<? extends Converter> converter();
//
//    String group() default "";
//
//    Class<? extends Validator> afterConvert() default Validator.class;
//}
