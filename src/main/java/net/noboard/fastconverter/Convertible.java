package net.noboard.fastconverter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Convertible {

    String group() default Converter.DEFAULT_GROUP;

    String tip() default Converter.DEFAULT_TIP;

    Class<? extends Converter> converter() default Converter.class;

}
