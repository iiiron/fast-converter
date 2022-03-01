package net.noboard.expression;

import net.noboard.Sex;
import net.noboard.fastconverter.FastConverter;
import org.junit.Test;
import org.springframework.util.Assert;

public class ExpressionTest {

    @Test
    public void test() {
        BeanA beanA = new BeanA();

        BeanB beanB = (BeanB) FastConverter.autoConvert(beanA);

        Assert.isTrue(beanB.getSex().equals(Sex.MAN.name()));
    }
}
