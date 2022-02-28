package net.noboard.fastconverter.support;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.parser.ConvertibleMap;
import net.noboard.fastconverter.parser.ImportParser;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
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
        if (convertibleBean.relevantClass() == Void.class && "".equals(convertibleBean.targetName())) {
            throw new ConvertException(String.format("you have to declare target of convert on @ConvertibleBean use 'targetName' or 'targetClass' at %s", beanClass.toString()));
        }
        if (convertibleBean.relevantClass() != Void.class
                && !"".equals(convertibleBean.relevantClassName())
                && !convertibleBean.relevantClass().getName().equals(convertibleBean.relevantClassName())) {
            throw new ConvertException("the attributes 'targetName' and 'targetClass' in annotation @ConvertibleBean must point the same class. or just declare one of the two");
        }

        return convertibleBean;
    }

    public static Class<?> getRelevantClass(ConvertibleBean convertibleBean) {
        try {
            if (convertibleBean.relevantClass() != Void.class) {
                return convertibleBean.relevantClass();
            } else {
                return Class.forName(convertibleBean.relevantClassName());
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

    /**
     * @param field 待解析AnnotatedElement
     * @param group 解析时使用的group
     * @return
     */
    public static ConvertibleMap parse(Field field, String group) {
        ImportParser importParser = AnnotatedElementUtils.getMergedAnnotation(field, ImportParser.class);
        if (importParser == null) {
            CMap cMap = new CMap();
            cMap.setAliasName(field.getName());
            cMap.setAbandon(false);
            cMap.setIgnoreNull(true);
            cMap.setConverter(null);
            cMap.setRelevantClass(null);
            cMap.setConvertContext(null);
            return cMap;
        }

        // 有解析器则用解析器的逻辑去构造ConvertibleMap
        return ConvertibleParserCache.get(importParser.clazz()).parse(field, group);
    }
}
