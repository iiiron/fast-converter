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
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字段分裂功能测试
 *
 * @author by wanxm
 * @date 2021/1/13 8:38 下午
 */
public class FieldDivisionTest {

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Test
    public void sourceTest() {
        Date now = new Date();
        BeanA beanA = new BeanA(now);
        BeanB beanB = FastConverter.autoConvert(beanA);
        Assert.isTrue(beanB.getTime().equals(now.getTime()));
        Assert.isTrue(beanB.getTimeString().equals(new SimpleDateFormat(FORMAT).format(now)));
    }

    @Test
    public void targetTest() {
        Date now = new Date();
        BeanC beanC = new BeanC(now);
        BeanD beanD = FastConverter.autoConvert(beanC, BeanD.class);
        Assert.isTrue(beanD.getTime().equals(now.getTime()));
        Assert.isTrue(beanD.getTimeString().equals(new SimpleDateFormat(FORMAT).format(now)));
    }

    @AllArgsConstructor
    @Data
    @ConvertibleBean(type = ConvertibleBeanType.SOURCE, targetClass = BeanB.class)
    public static class BeanA {
        @ConvertibleField(aliasName = "time", converter = DateToTimeStampConverterHandler.class)
        @ConvertibleField(aliasName = "timeString", converter = DateToFormatStringConverterHandler.class, tip = FORMAT)
        private Date time;
    }

    @Data
    public static class BeanB {
        private Long time;

        private String timeString;
    }

    @AllArgsConstructor
    @Data
    public static class BeanC {
        private Date time;
    }

    @Data
    @ConvertibleBean(targetClass = BeanC.class)
    public static class BeanD {
        @ConvertibleField(converter = DateToTimeStampConverterHandler.class)
        private Long time;

        @ConvertibleField(aliasName = "time", converter = DateToFormatStringConverterHandler.class)
        private String timeString;
    }
}
