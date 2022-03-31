package net.noboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleBeanType;
import net.noboard.fastconverter.FastConverter;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 * 预测转换器测试
 *
 * @date 2021/4/21 11:20 上午
 * @author by wanxm
 */
public class AutoSensingConvertTest {

    @Test
    public void test1() {
        BeanA beanA = new BeanA(BeanEnum.MAN, "wanxm", null);
        BeanB beanB = FastConverter.autoConvert(beanA);

        Assert.isTrue(BeanEnum.MAN.name().equals(beanB.getType()));
        Assert.isTrue("wanxm".equals(beanB.getName()));
    }

    @AllArgsConstructor
    @Data
    @ConvertibleBean(relevantClass = BeanB.class)
    public static class BeanA {
        private BeanEnum type;

        private String name;

        private String nullData;
    }

    @Data
    public static class BeanB {
        private String type;

        private String name;
    }

    public static enum BeanEnum {
        MAN,
    }
}
