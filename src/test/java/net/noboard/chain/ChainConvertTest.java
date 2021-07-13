package net.noboard.chain;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleBeanType;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.FastConverter;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;
import net.noboard.fastconverter.handler.auto.LongToDateSensingConverter;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * 链式转换测试
 *
 * @author by wanxm
 * @date 2021/7/6 10:21 下午
 */
public class ChainConvertTest {

    @Test
    public void test() {
        BeanA beanA = new BeanA();
        beanA.setTime(new Date().getTime());
        BeanB beanB = FastConverter.autoConvert(beanA);
        Assert.isTrue(beanB.getTime() != null && !beanB.getTime().isEmpty());
        Assert.isTrue(beanB.getTime2().getTime() == beanA.getTime());
    }

    @Test
    public void test2() {
        BeanC beanC = new BeanC();
        beanC.setTime(new Date().getTime());
        BeanD beanD = FastConverter.autoConvert(beanC, BeanD.class);
        Assert.isTrue(beanD.getTime() != null && !beanD.getTime().isEmpty());
        Assert.isTrue(beanD.getTime2().getTime() == beanC.getTime());
    }

    @Data
    @ConvertibleBean(relevantClass = BeanB.class, type = ConvertibleBeanType.SOURCE)
    public static class BeanA {
        @ConvertibleField(converter = LongToDateConverter.class)
        @ConvertibleField(converter = LongToDateConverter.class, aliasName = "time2")
        @ConvertibleField(converter = DateToFormatStringConverterHandler.class)
        private Long time;
    }

    @Data
    public static class BeanB {

        private String time;

        private Date time2;
    }

    @Data
    public static class BeanC {
        private Long time;
    }

    @Data
    @ConvertibleBean(relevantClass = BeanC.class)
    public static class BeanD {

        @ConvertibleField(converter = LongToDateConverter.class)
        @ConvertibleField(converter = DateToFormatStringConverterHandler.class)
        private String time;

        @ConvertibleField(converter = LongToDateConverter.class, aliasName = "time")
        private Date time2;
    }
}
