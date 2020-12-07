package net.noboard.demo;

import net.noboard.fastconverter.FastConverter;

import java.util.Date;

public class Test {

    @org.junit.Test
    public void test1() {
        Man man = new Man();
        man.setBirthday(new Date());
        man.setAge(20);
        man.setSex(true);
        man.setName("万相明");

        Object obj = FastConverter.autoConvert(man);
        System.out.println(obj);
    }
}
