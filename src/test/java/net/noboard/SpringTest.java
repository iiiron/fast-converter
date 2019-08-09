package net.noboard;

import net.noboard.bean.ChildA;
import net.noboard.bean.Man;
import net.noboard.fastconverter.handler.support.ConvertibleUtils;
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
    public void b() {
        Man m = new Man();
        ChildA childA = new ChildA();
        childA.setBirth(new Date());
        childA.setName("hello");
        List<ChildA> list = new ArrayList<>();
        list.add(childA);
        m.setChild(list);
        Object o = ConvertibleUtils.parse(Man.class, null, "A").getConverter().convert(m);
        System.out.println(o);
    }
}
