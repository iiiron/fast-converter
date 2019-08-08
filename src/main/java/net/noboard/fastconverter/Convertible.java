package net.noboard.fastconverter;

import net.noboard.fastconverter.handler.base.SkippingConverterHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Convertible {

    String group() default "default";

    Class<? extends Converter> converter() default SkippingConverterHandler.class;
}
