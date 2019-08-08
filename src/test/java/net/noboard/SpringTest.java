package net.noboard;

import net.noboard.bean.Man;
import org.junit.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;

public class SpringTest {
    @Test
    public void a() {
        Object annotatedElement = AnnotatedElementUtils.isAnnotated(Man.class, "net.noboard.fastconverter.Convertible");
        System.out.println(annotatedElement);
    }
}
