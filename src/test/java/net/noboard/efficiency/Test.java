package net.noboard.efficiency;

import com.sun.jmx.remote.internal.ArrayQueue;
import net.noboard.SpeedCheck;
import net.noboard.fastconverter.FastConverter;
import net.noboard.fastconverter.handler.DateToTimeStampStringConverterHandler;

import java.util.*;

public class Test {
    @org.junit.Test
    public void a() {
        List<BeanA> list = init();
//        FastConverter.customDefaultConverters()
//                .addFirst(new DateToTimeStampStringConverterHandler());
        Object o = SpeedCheck.check(a -> FastConverter.autoConvert(list));
        System.out.println("a");
        SpeedCheck.check(a -> FastConverter.autoConvert(list));
        SpeedCheck.check(a -> FastConverter.autoConvert(list));
        SpeedCheck.check(a -> FastConverter.autoConvert(list));
        SpeedCheck.check(a -> FastConverter.autoConvert(list));
    }

    private static List<BeanA> init() {
        List<BeanA> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            BeanA beanA = new BeanA();
            beanA.setAge(i);
            beanA.setBirthday(new Date());
            beanA.setName("job");

            Son son = new Son();
            son.setName("steve");
            son.setBirthday(new Date());
            son.setSex(true);

            List<Son> sons = new ArrayList<>();
            sons.add(son);
            beanA.setSons(sons);

            list.add(beanA);
        }
        return list;
    }
}
