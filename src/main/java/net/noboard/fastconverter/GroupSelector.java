package net.noboard.fastconverter;

import net.noboard.fastconverter.parser.ConvertibleFieldParser;
import net.noboard.fastconverter.parser.ImportParser;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@ConvertibleField
public @interface GroupSelector {
    @AliasFor(annotation = ConvertibleField.class)
    String group() default Converter.DEFAULT_GROUP;

    @AliasFor(annotation = ConvertibleField.class, value = "tip")
    String beanGroup() default Converter.DEFAULT_GROUP;
}
