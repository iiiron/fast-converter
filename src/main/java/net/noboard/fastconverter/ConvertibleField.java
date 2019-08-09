package net.noboard.fastconverter;

import net.noboard.fastconverter.handler.base.SkippingConverterHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ImportParser(clazz = ConvertibleFieldParser.class)
@Repeatable(ConvertibleFields.class)
@Convertible
public @interface ConvertibleField {
    @AliasFor(annotation = Convertible.class)
    String group() default Convertible.defaultGroup;

    @AliasFor(annotation = Convertible.class)
    Class<? extends Converter> converter() default Converter.class;

    boolean abandon() default false;

    String nameTo() default "";

    @AliasFor(annotation = Convertible.class)
    String tip() default Convertible.defaultTip;
}
