package net.noboard.fastconverter;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ConvertibleBeans.class)
@Convertible
public @interface ConvertibleBean {
    @AliasFor(annotation = Convertible.class)
    String group() default Converter.DEFAULT_GROUP;

    @Deprecated
    @AliasFor("relevantClassName")
    String targetName() default "";

    @AliasFor("targetName")
    String relevantClassName() default "";

    @Deprecated
    @AliasFor("relevantClass")
    Class<?> targetClass() default Void.class;

    @AliasFor("targetClass")
    Class<?> relevantClass() default Void.class;
}
