package net.noboard.fastconverter;

import java.lang.annotation.*;

/**
 * @author wanxm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(FieldConverterContainer.class)
public @interface FieldConverter {
    String tip() default "";

    Class<? extends Converter> converter();

    Class<? extends FieldValidator> afterConvert() default FieldValidator.class;
}
