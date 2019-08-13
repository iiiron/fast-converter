package net.noboard.fastconverter;

import net.noboard.fastconverter.parser.ConvertibleBeanParser;
import net.noboard.fastconverter.parser.ImportParser;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ConvertibleBeans.class)
@ImportParser(clazz = ConvertibleBeanParser.class)
@Convertible
public @interface ConvertibleBean {

    @AliasFor("tip")
    String group() default Converter.DEFAULT_GROUP;

    @AliasFor("group")
    String tip() default Converter.DEFAULT_GROUP;

    String targetName() default "";

    Class targetClass() default Void.class;

}
