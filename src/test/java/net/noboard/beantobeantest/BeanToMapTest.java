package net.noboard.beantobeantest;

import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.base.BeanToMapConverterHandler;
import net.noboard.fastconverter.handler.base.CommonFilterBaseConverterHandler;
import org.junit.Test;

import java.util.Map;

public class BeanToMapTest {
    @Test
    public void a() {
        CommonFilterBaseConverterHandler commonFilterBaseConverterHandler =
                new CommonFilterBaseConverterHandler(new CommonConverterFilter().addLast(BeanToMapConverterHandler::new));

        HumanA humanA = BeanToBeanTest.init();

        Map map = (Map) commonFilterBaseConverterHandler.convert(humanA);

        System.out.println(map);
    }
}
