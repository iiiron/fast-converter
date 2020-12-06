package net.noboard.fastconverter.handler.core.bean;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;

@Data
@Builder
public class BeanMapping {
    private static final ThreadLocal<LinkedList<BeanMapping>> sourceClass = new ThreadLocal<>();

    static {
        sourceClass.set(new LinkedList<>());
    }

    private Class<?> source;
    private Class<?> target;

    public static void push(Class<?> source, Class<?> target){
        sourceClass.get().addLast(BeanMapping.builder().source(source).target(target).build());
    }

    public static BeanMapping current(){
        return sourceClass.get().getLast();
    }

    public static BeanMapping pop(){
        return sourceClass.get().removeLast();
    }

    public static void clear(){
        sourceClass.get().clear();
    }
}
