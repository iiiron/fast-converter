package net.noboard.beantobeantest;

import net.noboard.bean.HumanA;
import net.noboard.fastconverter.filter.CommonSkipConverterFilter;
import net.noboard.fastconverter.handler.base.BeanToMapConverterHandler;
import net.noboard.fastconverter.handler.base.CommonFilterBaseConverterHandler;
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
}
