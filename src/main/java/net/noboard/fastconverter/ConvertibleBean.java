package net.noboard.fastconverter;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ConvertibleBeans.class)
@ImportParser(clazz = ConvertibleBeanParser.class)
@Convertible
public @interface ConvertibleBean {

    @AliasFor(annotation = Convertible.class)
    String group() default Convertible.defaultGroup;

    @AliasFor(annotation = Convertible.class)
    String tip() default Convertible.defaultGroup;

    @AliasFor(annotation = Convertible.class)
    Class<? extends Converter> converter() default Converter.class;

    String targetName() default "";

    Class targetClass() default Void.class;

    boolean nested() default true;
}
