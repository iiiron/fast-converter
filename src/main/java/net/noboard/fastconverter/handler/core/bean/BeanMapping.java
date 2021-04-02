package net.noboard.fastconverter.handler.core.bean;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;

@Data
@Builder
public class BeanMapping {
    private static final ThreadLocal<LinkedList<BeanMapping>> sourceClass = new ThreadLocal<>();

    private Class<?> source;
    private Class<?> target;

    public static void push(Class<?> source, Class<?> target) {
        init();
        sourceClass.get().addLast(BeanMapping.builder().source(source).target(target).build());
    }

    public static BeanMapping current() {
        if (hasMapping()) {
            return sourceClass.get().getLast();
        } else {
            return null;
        }
    }

    public static BeanMapping pop() {
        init();
        return sourceClass.get().removeLast();
    }

    public static boolean hasMapping() {
        return !CollectionUtils.isEmpty(sourceClass.get());
    }

    public static void clear() {
        if (sourceClass.get() != null) {
            sourceClass.get().clear();
        }
    }

    private static void init() {
        if (sourceClass.get() == null) {
            sourceClass.set(new LinkedList<>());
        }
    }
}
