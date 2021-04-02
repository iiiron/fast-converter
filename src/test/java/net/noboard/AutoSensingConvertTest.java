package net.noboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleBeanType;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.FastConverter;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;
import net.noboard.fastconverter.handler.DateToTimeStampConverterHandler;
import org.junit.Test;

import java.util.Date;

public class AutoSensingConvertTest {

    @Test
    public void test1() {
        BeanA beanA = new BeanA(BeanEnum.MAN);
        BeanB beanB = FastConverter.autoConvert(beanA);
    }

    @AllArgsConstructor
    @Data
    @ConvertibleBean(type = ConvertibleBeanType.SOURCE, targetClass = BeanB.class)
    public static class BeanA {
        private BeanEnum type;
    }

    @Data
    public static class BeanB {
        private String type;
    }

    public static enum BeanEnum {
        MAN,
    }
}
