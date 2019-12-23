package net.noboard.demo;


import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.BooleanToNumberConverterHandler;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;
import net.noboard.fastconverter.handler.NumberToBigDecimalConverterHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@ConvertibleField(converter = BooleanToNumberConverterHandler.class)
@ConvertibleField(converter = NumberToBigDecimalConverterHandler.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ToBig {
}
