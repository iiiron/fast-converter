package net.noboard.fastconverter.support;

import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConvertibleFieldCache {
    private static Map<AnnotatedElement, Set<ConvertibleField>> map = new HashMap<>();

    public static Set<ConvertibleField> get(AnnotatedElement field) {
        if (map.containsKey(field)) {
            return map.get(field);
        }
        Set<ConvertibleField> set = AnnotatedElementUtils.getMergedRepeatableAnnotations(field, ConvertibleField.class);
        map.put(field, set);
        return set;
    }
}
