package net.noboard.autosensing;

import lombok.extern.slf4j.Slf4j;
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

}
