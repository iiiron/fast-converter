package net.noboard.beantobeantest;

import net.noboard.bean.HumanA;
import net.noboard.fastconverter.filter.CommonSkipConverterFilter;
import net.noboard.fastconverter.handler.base.ArrayToArrayConverterHandler;
import net.noboard.fastconverter.handler.base.BeanToMapConverterHandler;
import net.noboard.fastconverter.handler.base.CommonFilterBaseConverterHandler;
import net.noboard.fastconverter.handler.base.SkippingConverterHandler;
import org.junit.Test;

import java.util.Map;

public class BeanToMapTest {
    @Test
    public void a() {
        BeanToMapConverterHandler beanToMapConverterHandler =
                new BeanToMapConverterHandler(new CommonSkipConverterFilter().addLast(BeanToMapConverterHandler::new));

        HumanA humanA = BeanToBeanTest.init();

        Object map = beanToMapConverterHandler.convertAndVerify(humanA);

        System.out.println(map);
    }

    @Test
    public void b() {
//        int[] a = {1,2,3};
//        ArrayToArrayConverterHandler<int[],int[]> arrayToArrayConverterHandler = new ArrayToArrayConverterHandler<>(new CommonSkipConverterFilter());
//        int[] b = arrayToArrayConverterHandler.convert(a);
//        System.out.println(b);
    }
}
