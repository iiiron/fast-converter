package net.noboard.sourcelab;

import com.alibaba.fastjson.JSON;
import net.noboard.fastconverter.FastConverter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SourceBeanTest {

    @Test
    public void tt() {
        BeanC beanC = new BeanC();
        beanC.setAge(20);
        List<BeanC> name = new ArrayList<>();
        name.add(beanC);

        BeanC tag = new BeanC();
        tag.setAge(100);

        List<String> strings = new ArrayList<>();
        strings.add("wanxm");

        BeanA beanA = new BeanA();
        beanA.setAge(27);
        beanA.setName(name);
        beanA.setTag(tag);
        beanA.setStringList(strings);

        Object result = FastConverter.autoConvert(beanA, BeanB.class, "a");
        System.out.println(JSON.toJSONString(result));
    }

}
