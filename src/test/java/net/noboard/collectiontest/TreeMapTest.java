package net.noboard.collectiontest;

import net.noboard.bean.Man;
import net.noboard.fastconverter.filter.CommonSkipConverterFilter;
import net.noboard.fastconverter.handler.ObjectToJsonStringConverterHandler;

import java.util.TreeSet;

public class TreeMapTest {
    public static void main(String[] args) {
        TreeSet<Man> men = new TreeSet<Man>();
        Man man = new Man();
        man.setName("a");

        Man man2 = new Man();
        man2.setName("b");

//        men.add(new Man());
        men.add(man);
        men.add(man2);

//        System.out.println(new ObjectToJsonStringConverterHandler(new CommonSkipConverterFilter()).convert(men));
    }
}
