package net.noboard.fastconverter;

import net.noboard.fastconverter.handler.base.SkippingConverterHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Convertible
public @interface ConvertBean {
    @AliasFor(annotation = Convertible.class)
    String group() default "default";

    @AliasFor(annotation = Convertible.class)
    Class<? extends Converter> converter() default SkippingConverterHandler.class;

    boolean abandon() default false;
}
