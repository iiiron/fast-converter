package net.noboard.fastconverter.handler.support;

import net.noboard.fastconverter.ConvertBean;
import net.noboard.fastconverter.Convertible;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.util.Set;

/**
 * @Convertible注解分组器
 */
public class ConvertibleGrouper {
    public static AnnotationAttributes processConvertBean(Class bean, String group) {
        Set<ConvertBean> set = AnnotatedElementUtils.findMergedRepeatableAnnotations(bean, ConvertBean.class);
        System.out.println(AnnotatedElementUtils.getAllAnnotationAttributes(bean, "net.noboard.fastconverter.ConvertBeans"));
        for (ConvertBean convertible : set) {
            System.out.println(convertible.group());
        }
        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(bean, "net.noboard.fastconverter.Convertibles");
        ConvertBean[] annotationAttributes1 = (ConvertBean[]) annotationAttributes.getAnnotationArray("value", ConvertBean.class);
        for (ConvertBean annotationAttributes2 : annotationAttributes1) {
            System.out.println(AnnotatedElementUtils.findMergedAnnotation(annotationAttributes2.getClass(), Convertible.class));
        }
        return null;
    }

}
