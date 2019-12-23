package net.noboard.demo;

import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ConvertibleField(converter = DateToFormatStringConverterHandler.class, tip = "yyyy-MM-dd")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DateFormat {
}
