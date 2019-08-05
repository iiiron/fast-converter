package net.noboard.commontest;

import net.noboard.fastconverter.handler.NumberToStringConverterHandler;

public class CommonTest {
    public static void main(String[] args) {
        NumberToStringConverterHandler numberToStringConverterHandler = new NumberToStringConverterHandler();
        Object o = numberToStringConverterHandler.convertAndVerify(100);
        System.out.println(o);
    }
}
