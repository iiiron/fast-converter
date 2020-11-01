package net.noboard.fastconverter;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ConvertibleBeans.class)
@Convertible
public @interface ConvertibleBean {

    @AliasFor("tip")
    String group() default Converter.DEFAULT_GROUP;

    @AliasFor("group")
    String tip() default Converter.DEFAULT_GROUP;

    String targetName() default "";

    Class<?> targetClass() default Void.class;

    ConvertibleBeanType type() default ConvertibleBeanType.TARGET;
}
