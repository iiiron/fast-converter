package net.noboard.efficiency;

import com.sun.jmx.remote.internal.ArrayQueue;
import net.noboard.SpeedCheck;
import net.noboard.fastconverter.FastConverter;

import java.util.*;

public class Test {
    @org.junit.Test
    public void a() {
        List<BeanA> list = init();
        Object o = SpeedCheck.check(a -> FastConverter.autoConvert(list));
//        System.out.println(o);
    }

    private static List<BeanA> init() {
        List<BeanA> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            BeanA beanA = new BeanA();
            Son son = new Son();
            son.setName("steve");
            son.setBirthday(new Date());
            beanA.setAge(i);
            beanA.setBirthday(new Date());
            beanA.setName("job");
            List<Son> sons = new ArrayList<>();
            sons.add(son);
            beanA.setSons(sons);
            list.add(beanA);
        }
        return list;
    }
}
