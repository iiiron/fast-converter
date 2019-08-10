package net.noboard.beantobeantest;

import net.noboard.bean.HumanA;
import net.noboard.fastconverter.filter.CommonSkipConverterFilter;
import net.noboard.fastconverter.handler.BeanToMapConverterHandler;
import net.noboard.fastconverter.handler.core.CommonFilterBaseConverterHandler;
import org.junit.Test;

import java.util.Map;

public class BeanToMapTest {
    @Test
    public void a() {
        CommonFilterBaseConverterHandler commonFilterBaseConverterHandler =
                new CommonFilterBaseConverterHandler(new CommonSkipConverterFilter().addLast(BeanToMapConverterHandler::new));

        HumanA humanA = BeanToBeanTest.init();

        Map map = (Map) commonFilterBaseConverterHandler.convert(humanA);

        System.out.println(map);
    }
}
