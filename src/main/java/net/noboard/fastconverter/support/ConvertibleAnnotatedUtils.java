package net.noboard.fastconverter.support;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.parser.ConvertibleMap;
import net.noboard.fastconverter.parser.ConvertibleParser;
import net.noboard.fastconverter.parser.ImportParser;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Convertible注解分组器
 */
public class ConvertibleAnnotatedUtils {
    /**
     * 根据分组过滤出class上的@ConvertibleBean注解数据
     *
     * @param beanClass
     * @param group
     * @return
     */
    public static ConvertibleBean getMergedConvertBean(AnnotatedElement beanClass, String group) {
        Set<ConvertibleBean> set = ConvertibleBeanCache.get(beanClass);
        ConvertibleBean convertibleBean = null;
        for (ConvertibleBean bean : set) {
            GroupUtils.requireGroup(bean.group(), bean.getClass());
            if (GroupUtils.checkGroup(bean.group(), group)) {
                convertibleBean = bean;
            }
        }

        if (convertibleBean == null) {
//            throw new ConvertException(String.format("no @ConvertibleBean annotation agree with group '%s', on bean %s",
//                    group,
//                    beanClass.toString()));
            return null;
        }
        if (convertibleBean.targetClass() == Void.class && "".equals(convertibleBean.targetName())) {
            throw new ConvertException(String.format("you have to declare target of convert on @ConvertibleBean use 'targetName' or 'targetClass' at %s", beanClass.toString()));
        }
        if (convertibleBean.targetClass() != Void.class
                && !"".equals(convertibleBean.targetName())
                && !convertibleBean.targetClass().getName().equals(convertibleBean.targetName())) {
            throw new ConvertException("the attributes 'targetName' and 'targetClass' in annotation @ConvertibleBean must point the same class. or just declare one of the two");
        }

        return convertibleBean;
    }

    public static Class<?> getTargetClass(ConvertibleBean convertibleBean) {
        try {
            if (convertibleBean.targetClass() != Void.class) {
                return convertibleBean.targetClass();
            } else {
                return Class.forName(convertibleBean.targetName());
            }
        } catch (ClassNotFoundException e) {
            throw new ConvertException(String.format("the target class %s in @ConvertibleBean cannot be find", convertibleBean.targetName()), e);
        }
    }


    /**
     * 根据分组过滤出Field上的@ConvertibleField注解数据，以注解申明顺序排序
     *
     * @param field
     * @param group
     * @return
     */
    public static LinkedHashSet<ConvertibleField> getMergedConvertField(AnnotatedElement field, String group) {
        Set<ConvertibleField> set = ConvertibleFieldCache.get(field);
        if (set == null || set.size() < 1) {
            return null;
        }

        LinkedHashSet<ConvertibleField> linkedHashSet = new LinkedHashSet<>();
        for (ConvertibleField convertibleField : set) {
            GroupUtils.requireGroup(convertibleField.group(), convertibleField.getClass());
            if (convertibleField.group().equals(group)) {
                linkedHashSet.add(convertibleField);
            }
        }

        return linkedHashSet;
    }

    public static ConvertibleMap parse(AnnotatedElement annotatedElement) {
        return parse(annotatedElement, Converter.DEFAULT_GROUP);
    }

    /**
     * @param annotatedElement 待解析AnnotatedElement
     * @param group            解析时使用的group
     * @return
     */
    public static ConvertibleMap parse(AnnotatedElement annotatedElement, String group) {
        ImportParser importParser = AnnotatedElementUtils.getMergedAnnotation(annotatedElement, ImportParser.class);
        if (importParser == null) {
            CMap cMap = new CMap();
            cMap.setAbandon(false);
            cMap.setRetainNull(true);
            return cMap;
        }
        ConvertibleParser parser = ConvertibleParserCache.get(importParser.clazz());
        return parser.parse(annotatedElement, group);
    }
}
