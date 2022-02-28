package net.noboard.fastconverter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Convertible {

    String group() default Converter.DEFAULT_GROUP;

    Class<? extends Converter> converter() default Converter.class;

}
