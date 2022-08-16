package net.noboard;


import lombok.AllArgsConstructor;
import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleBeanType;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.FastConverter;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;
import net.noboard.fastconverter.handler.DateToLongConverterHandler;
import net.noboard.fastconverter.handler.FormatStringToDateConverterHandler;
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

    @Test
    public void sourceTest() {
        Date now = new Date();
        BeanA beanA = new BeanA(now);
        BeanB beanB = FastConverter.autoConvert(beanA);
        Assert.isTrue(beanB.getTime().equals(now.getTime()));
        Assert.isTrue(beanB.getTimeString().equals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now)));
    }

    @Test
    public void targetTest() {
        Date now = new Date();
        BeanC beanC = new BeanC();
        beanC.setTime(now);
        BeanD beanD = FastConverter.downCasting(beanC, BeanD.class);
        Assert.isTrue(beanD.getTimeString().equals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now)));

        beanC = FastConverter.upCasting(beanD);
        Assert.isTrue(beanD.getTimeString().equals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beanC.getTime())));
    }

    @AllArgsConstructor
    @Data
    @ConvertibleBean(relevantClass = BeanB.class)
    public static class BeanA {
        @ConvertibleField(aliasName = "time", converter = DateToLongConverterHandler.class)
        @ConvertibleField(aliasName = "timeString", converter = DateToFormatStringConverterHandler.class, context = "'yyyy-MM-dd HH:mm:ss'")
        private Date time;
    }

    @Data
    public static class BeanB {
        private Long time;

        private String timeString;
    }

    @Data
    public static class BeanC {
        private Date time;
    }

    @Data
    @ConvertibleBean(relevantClass = BeanC.class)
    public static class BeanD {
        @ConvertibleField(aliasName = "time", downCastingConverter = DateToFormatStringConverterHandler.class, upCastingConverter = FormatStringToDateConverterHandler.class)
        private String timeString;
    }
}
