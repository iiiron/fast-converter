package net.noboard;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleBeanType;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.FastConverter;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * 容器字段测试
 *
 * @date 2021/1/29 5:55 下午
 * @author by wanxm
 */
public class CollectionTest {

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    DateToFormatStringConverterHandler dateToFormatStringConverterHandler = new DateToFormatStringConverterHandler();

    @Test
    public void test1() {
        Date now = new Date();

        ListA listA = new ListA(Lists.newArrayList(new ItemA(now)));

        ListB listB = FastConverter.autoConvert(listA, ListB.class);
        for (ItemB itemB : listB.getList()) {
            Assert.isTrue(itemB.getTime().equals(dateToFormatStringConverterHandler.convert(now)), "itemB 中的字符串与 iteamA 的时间不一致");
        }

        listB = FastConverter.autoConvert(listA);
        for (ItemB itemB : listB.getList()) {
            Assert.isTrue(itemB.getTime().equals(dateToFormatStringConverterHandler.convert(now)), "itemB 中的字符串与 iteamA 的时间不一致");
        }
    }

    @Test
    public void test2() {
        Date now = new Date();

        ListA listA = new ListA(Lists.newArrayList(new ItemA(now)));

        ListB listB = FastConverter.downCasting(listA, ListB.class, "group2");
        for (ItemB itemB : listB.getList()) {
            Assert.isTrue(itemB.getTime().equals(dateToFormatStringConverterHandler.convert(now)), "itemB 中的字符串与 iteamA 的时间不一致");
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ConvertibleBean(relevantClass = ItemB.class)
    public static class ItemA {
        @ConvertibleField(converter = DateToFormatStringConverterHandler.class)
        private Date time;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ConvertibleBean(relevantClass = ListB.class)
    public static class ListA {
        List<ItemA> list;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ConvertibleBean(relevantClass = ItemA.class)
    @ConvertibleBean(relevantClass = ItemA.class, group = "group2")
    public static class ItemB {
        @ConvertibleField(converter = DateToFormatStringConverterHandler.class)
        private String time;
    }

    @NoArgsConstructor
    @Data
    @ConvertibleBean(relevantClass = ListA.class)
    @ConvertibleBean(relevantClass = ListA.class, group = "group2")
    public static class ListB {
        List<ItemB> list;
    }
}
