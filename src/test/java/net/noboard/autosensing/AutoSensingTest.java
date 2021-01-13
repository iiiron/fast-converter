package net.noboard.autosensing;

import net.noboard.fastconverter.FastConverter;
import net.noboard.fastconverter.handler.AutoSensingConverter;
import org.junit.Test;

public class AutoSensingTest {

    @Test
    public void test() {
        AutoSensingConverter autoSensingConverter = new AutoSensingConverter();
        Object obj = autoSensingConverter.convert("MAN", Human.class.getName());
        System.out.println(obj);

        obj = autoSensingConverter.convert(Human.LADY, String.class.getName());
        System.out.println(obj);
    }

    @Test
    public void test2() {
        Object obj = FastConverter.autoConvert(BeanA.builder().human("MAN").name("wanxm").build(), BeanB.class);
        System.out.println(obj);

    }

}
