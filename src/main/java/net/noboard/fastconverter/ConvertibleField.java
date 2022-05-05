package net.noboard.fastconverter;

import net.noboard.fastconverter.parser.ConvertibleFieldParser;
import net.noboard.fastconverter.parser.ImportParser;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@ImportParser(clazz = ConvertibleFieldParser.class)
@Repeatable(ConvertibleFields.class)
@Convertible
public @interface ConvertibleField {
    @AliasFor(annotation = Convertible.class)
    String group() default Converter.DEFAULT_GROUP;

    String context() default Converter.DEFAULT_TIP;

    @AliasFor(annotation = Convertible.class)
    Class<? extends Converter> converter() default Converter.class;

    boolean abandon() default false;

    String aliasName() default "";

    boolean ignoreNull() default true;
}
