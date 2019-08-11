package net.noboard.fastconverter.support;

import net.noboard.fastconverter.*;
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
//        GroupUtils.requireGroup(group);

        Set<ConvertibleBean> set = AnnotatedElementUtils.findMergedRepeatableAnnotations(beanClass, ConvertibleBean.class);
        ConvertibleBean convertibleBean = null;
        for (ConvertibleBean bean : set) {
            GroupUtils.requireGroup(bean.group(), bean.getClass());
            if (GroupUtils.checkGroup(bean.group(), group)) {
                convertibleBean = bean;
            }
        }

        if (convertibleBean == null) {
            throw new ConvertException(String.format("no @ConvertibleBean annotation agree with group '%s', on bean %s",
                    group,
                    beanClass.toString()));
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

    /**
     * 根据分组过滤出Field上的@ConvertibleField注解数据，以注解申明顺序排序
     *
     * @param field
     * @param group
     * @return
     */
    public static LinkedHashSet<ConvertibleField> getMergedConvertField(AnnotatedElement field, String group) {
//        GroupUtils.requireGroup(group);

        Set<ConvertibleField> set = AnnotatedElementUtils.findMergedRepeatableAnnotations(field, ConvertibleField.class);
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
            return parser.parse(annotatedElement, tip, group);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ConvertibleMap factory(String tip, String group, Converter converter) {
        return new ConvertibleMap() {
            @Override
            public void setAbandon(boolean abandon) {

            }

            @Override
            public boolean isAbandon() {
                return false;
            }

            @Override
            public void setRetainNull(boolean isSkipNull) {

            }

            @Override
            public boolean isRetainNull() {
                return true;
            }

            @Override
            public void setTip(String tip) {

            }

            @Override
            public String getTip() {
                return tip;
            }

            @Override
            public void setConverter(Converter converter) {

            }

            @Override
            public Converter getConverter() {
                return converter;
            }

            @Override
            public void join(ConvertibleMap convertibleMap) {

            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public ConvertibleMap next() {
                return null;
            }
        };
    }
}
