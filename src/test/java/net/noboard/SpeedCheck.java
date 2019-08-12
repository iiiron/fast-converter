package net.noboard;

import java.util.function.Function;

public class SpeedCheck {
    public static  <T, R> R check(Function<T, R> function) {
        long start = System.nanoTime();
        R r = function.apply(null);
        long end = System.nanoTime();
        System.out.println(String.format("耗时：%d ms", (end - start) / 1000 / 1000));
        return r;
    }
}
