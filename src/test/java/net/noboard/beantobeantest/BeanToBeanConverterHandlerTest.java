package net.noboard.beantobeantest;

import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.filter.AbstractConverterFilter;
import net.noboard.fastconverter.handler.BooleanToStringConverterHandler;
import net.noboard.fastconverter.handler.base.*;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class BeanToBeanConverterHandlerTest {
    public static void main(String[] args) {
        HumanA humanA = new HumanA();
        Man man = new Man();
        man.setName("wanxm");
        man.setBirthday(new Date());
        humanA.setHuman(man);
        HumanB humanB = (HumanB) BeanToBeanConverterHandler.beanCopy().convert(humanA, HumanB.class);
        System.out.println("1");
    }

    @Test
    public void a() {
        CommonFilterBaseConverterHandler commonFilterBaseConverterHandler = new CommonFilterBaseConverterHandler(new AbstractConverterFilter() {
            @Override
            protected void initConverters(List<Converter<?, ?>> converters) {
                converters.add(new NullConverterHandler());
                converters.add(new SkippingConverterHandler(SkippingConverterHandler.BASIC_DATA_TYPE));
                converters.add(new SkippingConverterHandler(SkippingConverterHandler.COMMON_DATA_TYPE));
                converters.add(new CollectionToCollectionConverterHandler<>(this));
                converters.add(new ArrayToArrayConverterHandler<>(this));
                converters.add(new MapToMapConverterHandler<>(this));
                converters.add(new BeanToBeanConverterHandler<>(this, HumanA.class, HumanB.class));
                converters.add(new BeanToBeanConverterHandler<>(this, Man.class, Woman.class));
            }
        });

        HumanA humanA = factory();

        Object b = commonFilterBaseConverterHandler.convert(humanA);
        System.out.println(b);
    }

    @Test
    public void b() {
        BeanToBeanConverterHandler beanToBeanConverterHandler = new BeanToBeanConverterHandler((list -> {
            list.add(new NullConverterHandler());
            list.add(new SkippingConverterHandler(String.class, Integer.class));
            list.add(new BooleanToStringConverterHandler());
        }));
        HumanA humanA = factory();
        HumanB humanB = (HumanB) beanToBeanConverterHandler.convert(humanA, "net.noboard.beantobeantest.HumanB");
        System.out.println(humanB);
    }

    private HumanA factory() {
        HumanA humanA = new HumanA();

        Man man = new Man();
        man.setName("wanxm");
        man.setBirthday(new Date());
        man.setSex(true);
//        man.setAge(25);

        humanA.setHuman(man);

        return humanA;
    }
}
