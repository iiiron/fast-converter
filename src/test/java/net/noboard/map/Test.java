package net.noboard.map;

import net.noboard.fastconverter.FastConverter;

import java.util.HashMap;
import java.util.Map;

public class Test {
    @org.junit.Test
    public void a() {
        Map<Class, Object> map = new HashMap<>();
        map.put(Integer.class, Integer.valueOf(1));
        map.put(String.class, "2");
        Object o = FastConverter.autoConvert(map);
        System.out.println(o);
    }
}
