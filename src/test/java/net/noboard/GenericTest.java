package net.noboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleBeanType;
import net.noboard.fastconverter.FastConverter;
import org.junit.Test;
import org.springframework.util.Assert;

public class GenericTest {

    @Test
    public void test() {
        BeanA<EnumA> a = new BeanA<>(EnumA.MAN);
        BeanB b = FastConverter.autoConvert(a);

        Assert.isTrue(EnumA.MAN.name().equals(b.getFieldA()));
    }

    @Test
    public void test2(){
        BeanC beanC = new BeanC(EnumA.MAN.name());

        BeanE b = FastConverter.autoConvert(beanC, BeanE.class);

        Assert.isTrue(EnumA.MAN.equals(b.getFieldA()));
    }

    @AllArgsConstructor
    @Data
    @ConvertibleBean(targetClass = BeanB.class, type = ConvertibleBeanType.SOURCE)
    public static class BeanA<T> {
        private T fieldA;
    }

    @Data
    public static class BeanB {
        private String fieldA;
    }

    @AllArgsConstructor
    @Data
    public static class BeanC {
        private String fieldA;
    }

    @Data
    @ConvertibleBean(targetClass = BeanC.class)
    public static class BeanD<T> {
        protected T fieldA;
    }

    @Data
    @ConvertibleBean(targetClass = BeanC.class)
    public static class BeanE extends BeanD<EnumA> {

    }

    public enum EnumA {
        MAN;
    }
}
