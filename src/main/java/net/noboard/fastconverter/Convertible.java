package net.noboard.fastconverter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Convertible {

    String defaultGroup = "default";

    String defaultTip = "";

    String group() default defaultGroup;

    Class<? extends Converter> converter() default Converter.class;

    String tip() default defaultTip;

}
