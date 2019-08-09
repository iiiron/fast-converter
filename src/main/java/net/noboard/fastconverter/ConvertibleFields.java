package net.noboard.fastconverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Convertible
@ImportParser(clazz = ConvertibleFieldsParser.class)
public @interface ConvertibleFields {
    ConvertibleField[] value();
}
