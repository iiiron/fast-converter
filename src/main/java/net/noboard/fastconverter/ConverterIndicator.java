package net.noboard.fastconverter;

import java.lang.annotation.*;

/**
 * @author wanxm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Repeatable(ConverterIndicatorContainer.class)
public @interface ConverterIndicator {
    String tip() default "";

    Class<? extends Converter> converter();

    Class<? extends Validator> afterConvert() default Validator.class;
}
