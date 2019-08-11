package net.noboard;

import net.noboard.bean.ChildA;
import net.noboard.bean.Man;
import net.noboard.fastconverter.FastConverter;
import org.junit.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpringTest {
    @Test
    public void a() {
        Object annotatedElement = AnnotatedElementUtils.isAnnotated(Man.class, "net.noboard.fastconverter.Convertible");
        System.out.println(annotatedElement);
    }

    @Test
    public void c() {
        ChildA childA = new ChildA();
        childA.setName("wanxm");
        Object o = FastConverter.autoConvert(childA);
        System.out.println(o);
    }

    @Test
    public void b() {
        Man m = new Man();
        ChildA childA = new ChildA();
        childA.setBirthday(new Date());
        childA.setName("hello");
        List<ChildA> list = new ArrayList<>();
        list.add(childA);
        List<List<ChildA>> lists = new ArrayList<>();
        lists.add(list);
        m.setChild(lists);
        Object o = FastConverter.autoConvert(m,"b");
        System.out.println(o);
    }
}
