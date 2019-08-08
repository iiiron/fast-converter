package net.noboard.fastconverter;

import net.noboard.fastconverter.handler.base.SkippingConverterHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ConvertBeans.class)
@Convertible
public @interface ConvertBean {
    public static final String defaultGroup = "default";

    @AliasFor(annotation = Convertible.class)
    String group() default defaultGroup;

    @AliasFor(annotation = Convertible.class)
    Class<? extends Converter> converter() default SkippingConverterHandler.class;

    String targetName() default "";

    Class targetClass() default Void.class;
}
