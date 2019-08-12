package net.noboard.fastconverter.support;

import net.noboard.fastconverter.ConvertibleBean;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConvertibleBeanCache {
    private static Map<AnnotatedElement, Set<ConvertibleBean>> map = new HashMap<>();

    public static Set<ConvertibleBean> get(AnnotatedElement beanClass) {
        if (map.containsKey(beanClass)) {
            return map.get(beanClass);
        }
        Set<ConvertibleBean> set = AnnotatedElementUtils.getMergedRepeatableAnnotations(beanClass, ConvertibleBean.class);
        map.put(beanClass, set);
        return set;
    }
}
