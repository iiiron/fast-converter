package net.noboard.beantobeantest;

import net.noboard.bean.*;
import net.noboard.fastconverter.filter.CommonSkipConverterFilter;
import net.noboard.fastconverter.handler.base.BeanToBeanConverterHandler;
import org.junit.Test;

import java.util.*;

public class BeanToBeanTest {

    @Test
    public void a() {
//        BeanToBeanConverterHandler beanToBeanConverterHandler =
//                BeanToBeanConverterHandler.transferCustom(new CommonSkipConverterFilter());
//
//        HumanA humanA = init();
//        HumanB humanB = (HumanB) beanToBeanConverterHandler.convert(humanA, "net.noboard.bean.HumanB");
//        System.out.println(humanB);
    }

    public static HumanA init() {
        HumanA humanA = new HumanA();
//
//        Man man = new Man();
//        man.setName("wanxm");
//        man.setBirthday(new Date());
//        man.setAge(25);
//        man.setSex(true);
////        man.setAaa(new int[]{1, 2, 3});
//        man.setBbb(new Integer[]{1, 2, 3});
//        man.setFff(true);
//
//        Set sets = new HashSet();
//        sets.add("a");
//        sets.add("b");
//        sets.add("c");
//        man.setCcc(sets);
//
//        List<String>[] lists = new List[2];
//        List<String> stringList = new ArrayList<>();
//        stringList.add("hello");
//        lists[0] = stringList;
//        lists[1] = new ArrayList<>();
//        man.setDdd(lists);
//
//        Map<String, Date> map = new HashMap<>();
//        map.put("wanxm", new Date());
//        man.setEee(map);
//
//        List<ChildA> list = new ArrayList<>();
//        ChildA childA = new ChildA();
//        childA.setName("heiheihei");
//        childA.setBirthday(new Date());
//        list.add(childA);
//        humanA.setChildren(list);
//
//        humanA.setHuman(man);
        return humanA;
    }
}
