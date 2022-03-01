package net.noboard.enumConverter;

import net.noboard.Sex;
import net.noboard.fastconverter.FastConverter;
import org.junit.Test;
import org.springframework.util.Assert;

public class EnumTest {

    @Test
    public void t() {
        BeanB beanB = new BeanB();
        beanB.setSex(Sex.MAN.name());

        BeanA o = FastConverter.autoConvert(beanB);
        Assert.isTrue(o.getSex().equals(Sex.MAN));
    }
}
