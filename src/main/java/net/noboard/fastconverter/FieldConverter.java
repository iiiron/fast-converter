package net.noboard.fastconverter;

import net.noboard.fastconverter.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wanxm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldConverter {
    String tip() default "";

    Class<? extends Converter> converter();
}
