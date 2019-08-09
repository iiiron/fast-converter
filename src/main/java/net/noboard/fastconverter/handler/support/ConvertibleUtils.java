package net.noboard.fastconverter.handler.support;

import net.noboard.fastconverter.*;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Convertible注解分组器
 */
public class ConvertibleUtils {
    /**
     * 从bean上读取融合后的ConvertBean AnnotatedElement对象
     *
     * @param beanClass
     * @param group
     * @return
     */
    public static ConvertibleBean getMergedConvertBean(AnnotatedElement beanClass, String group) {
        requireGroup(group);

        Set<ConvertibleBean> set = AnnotatedElementUtils.findMergedRepeatableAnnotations(beanClass, ConvertibleBean.class);
        if (set == null || set.size() < 1) {
            return null;
        }
        for (ConvertibleBean bean : set) {
            requireAnnotationGroup(bean.group(), bean.getClass());
            if (GroupUtils.checkGroup(bean.group(), group)) {
                return bean;
            }
        }
        return null;
    }

    public static LinkedHashSet<ConvertibleField> getMergedConvertField(AnnotatedElement field, String group) {
        requireGroup(group);

        Set<ConvertibleField> set = AnnotatedElementUtils.findMergedRepeatableAnnotations(field, ConvertibleField.class);
        if (set == null || set.size() < 1) {
            return null;
        }

        LinkedHashSet<ConvertibleField> linkedHashSet = new LinkedHashSet<>();
        for (ConvertibleField convertibleField : set) {
            requireAnnotationGroup(convertibleField.group(), convertibleField.getClass());
            if (convertibleField.group().equals(group)) {
                linkedHashSet.add(convertibleField);
            }
        }

        return linkedHashSet;
    }

    public static ConvertibleMap parse(AnnotatedElement annotatedElement) {
        return parse(annotatedElement, Convertible.defaultTip, Convertible.defaultGroup);
    }

    public static ConvertibleMap parse(AnnotatedElement annotatedElement, String tip) {
        return parse(annotatedElement, tip, Convertible.defaultGroup);
    }

    /**
     * @param annotatedElement 待解析AnnotatedElement
     * @param tip              解析时使用的默认tip
     * @param group            解析时使用的group
     * @return
     */
    public static ConvertibleMap parse(AnnotatedElement annotatedElement, String tip, String group) {
        try {
            ImportParser importParser = AnnotatedElementUtils.getMergedAnnotation(annotatedElement, ImportParser.class);
            if (importParser == null) {
                return factory(tip, group, null);
            }
            ConvertibleParser parser = importParser.clazz().newInstance();
            parser.setAnnotatedElement(annotatedElement);
            parser.setGroup(group);
            parser.setTip(tip);
            return parser.parse();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ConvertibleMap factory(String tip, String group, Converter converter) {
        return new ConvertibleMap() {
            @Override
            public void setTip(String tip) {

            }

            @Override
            public String getTip() {
                return tip;
            }

            @Override
            public void setGroup(String group) {

            }

            @Override
            public String getGroup() {
                return group;
            }

            @Override
            public void setConverter(Converter converter) {

            }

            @Override
            public Converter getConverter() {
                return converter;
            }
        };
    }

    private static void requireGroup(String group) {
        if (group == null || "".equals(group)) {
            throw new IllegalArgumentException("the string argument named group can not be null or empty string");
        }
    }

    private static void requireAnnotationGroup(String group, Class groupOf) {
        if ("".equals(group)) {
            throw new IllegalArgumentException(String.format("attribute group of %s can not be empty string", groupOf.getName()));
        }
    }
}
