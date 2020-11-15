package net.noboard.fastconverter.handler.core.bean;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

@Data
@Builder
public class TargetBeanMapping {
    /**
     * key = group
     * value = beanMapping
     */
    private static final HashMap<String, ArrayList<BeanMapping>> sourceClass = new HashMap<>();

    private Class<?> source;
    private Class<?> target;

    public static void push(String group, Class<?> source, Class<?> target) {
        synchronized (sourceClass) {
            ArrayList<BeanMapping> beanMappings = sourceClass.computeIfAbsent(group, k -> new ArrayList<>());
            beanMappings.add(BeanMapping.builder().source(source).target(target).build());
        }
    }

    public static Class<?> getTarget(String group, Class<?> source) {
        BeanMapping beanMapping = sourceClass.get(group).stream().filter(o -> o.getSource().equals(source)).findFirst().orElse(null);
        if (beanMapping == null) {
            return null;
        } else {
            return beanMapping.getTarget();
        }
    }

}
