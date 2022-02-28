package net.noboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleBeanType;
import net.noboard.fastconverter.FastConverter;
import org.springframework.util.Assert;

import java.lang.reflect.Field;

public class GenericTest {

    private BeanA<EnumA> a = new BeanA<EnumA>(EnumA.MAN);

//    @Test
    public void test() {
        Field[] declaredFields = GenericTest.class.getDeclaredFields();

        BeanB b = FastConverter.autoConvert(a);

        Assert.isTrue(EnumA.MAN.name().equals(b.getFieldA()));
    }

//    @Test
    public void test2(){
        BeanC beanC = new BeanC(EnumA.MAN.name());

        BeanD b = FastConverter.autoConvert(beanC, BeanD.class);

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
