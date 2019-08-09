package net.noboard.fastconverter;

import net.noboard.fastconverter.handler.base.SkippingConverterHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ConvertibleBeans.class)
@ImportParser(clazz = ConvertibleBeanParser.class)
@Convertible
public @interface ConvertibleBean {

    @AliasFor("tip")
    String group() default Convertible.defaultGroup;

    @AliasFor("group")
    String tip() default Convertible.defaultGroup;

    @AliasFor(annotation = Convertible.class)
    Class<? extends Converter> converter() default Converter.class;

    String targetName() default "";

    Class targetClass() default Void.class;

    boolean nested() default true;
}
