package net.noboard.fastconverter;

import net.noboard.fastconverter.handler.base.SkippingConverterHandler;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(Convertibles.class)
public @interface Convertible {

    String group() default "default";

    Class<? extends Converter> converter() default SkippingConverterHandler.class;
}
