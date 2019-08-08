package net.noboard;

import net.noboard.bean.Man;
import net.noboard.fastconverter.handler.support.ConvertibleGrouper;
import org.junit.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;

public class SpringTest {
    @Test
    public void a() {
        Object annotatedElement = AnnotatedElementUtils.isAnnotated(Man.class, "net.noboard.fastconverter.Convertible");
        System.out.println(annotatedElement);
    }

    @Test
    public void b() {
        ConvertibleGrouper.processConvertBean(Man.class, "A");
    }
}
