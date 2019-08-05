package net.noboard.fastconverter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BeanConverterIndicator {
    Class targetType() default Void.class;

//    Class<? extends Validator> afterConvert() default Validator.class;
}
