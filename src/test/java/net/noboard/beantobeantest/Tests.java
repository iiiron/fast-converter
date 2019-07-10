package net.noboard.beantobeantest;

import net.noboard.fastconverter.handler.base.BeanToBeanConverterHandler;

import java.util.Date;

public class Tests {
    public static void main(String[] args) {
        HumanA humanA = new HumanA();
        net.noboard.beantobeantest.Man man = new net.noboard.beantobeantest.Man();
        man.setName("wanxm");
        man.setBirthday(new Date());
        humanA.setHuman(man);
        HumanB humanB = (HumanB) BeanToBeanConverterHandler.beanCopy.convert(humanA, HumanB.class);
        System.out.println("1");
    }
}
