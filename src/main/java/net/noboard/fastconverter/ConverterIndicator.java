package net.noboard.fastconverter;

import java.lang.annotation.*;

/**
 * @author wanxm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Repeatable(FieldConverterContainer.class)
public @interface ConverterIndicator {
    String tip() default "";

    Class<? extends Converter> converter() default Converter.class;

    Class<? extends Validator> afterConvert() default Validator.class;
}
