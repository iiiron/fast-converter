package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.parser.ConvertibleMap;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import javax.validation.constraints.NotNull;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractBeanConverter<T, K> implements BeanConverter<T, K> {

    private ConverterFilter converterFilter;

    public AbstractBeanConverter(ConverterFilter converterFilter) {
        this.converterFilter = converterFilter;
    }

    @Override
    public void setFilter(ConverterFilter converterFilter) {
        this.converterFilter = converterFilter;
    }

    @Override
    public ConverterFilter getFilter() {
        return this.converterFilter;
    }


    @Override
    public Object convert(Object from, String group) throws ConvertException {
        Object targetObj = instantiateTarget(getTargetClass(from, group));

        Map<String, Object> map = parse(from, group, targetObj.getClass());

        try {
            for (PropertyDescriptor targetPD : Introspector.getBeanInfo(targetObj.getClass()).getPropertyDescriptors()) {
                if ("class".equals(targetPD.getName())) {
                    continue;
                }
                try {
                    targetPD.getWriteMethod().invoke(targetObj, map.get(targetPD.getName()));
                } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                    throw new ConvertException(
                            String.format("Conversion result type mismatch. at field '%s' of target class %s", targetPD.getName(), targetObj.getClass().getName()));
                }
            }
        } catch (IntrospectionException e) {
            throw new ConvertException(e);
        }

        return targetObj;
    }

    protected Class<?> getTargetClass(Object source, String group) {
        Objects.requireNonNull(source);

        if (BeanMapping.hasMapping()) {
            return BeanMapping.current().getTarget();
        } else {
            ConvertibleBean convertibleBean = ConvertibleAnnotatedUtils.getMergedConvertBean(source.getClass(), group);
            if (ConvertibleBeanType.SOURCE.equals(convertibleBean.type())) {
                return ConvertibleAnnotatedUtils.getTargetClass(convertibleBean);
            }

            throw new ConvertException(
                    String.format("the source bean %s defect @ConvertibleBean with type of ConvertibleBeanType.SOURCE", source.getClass()));
        }
    }

    protected Object instantiateTarget(Class<?> target) {
        try {
            return target.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new ConvertException(
                    String.format("the target class %s can not be implemented", target), e);
        }
    }

    abstract protected Map<String, Object> parse(Object source, String group, Class<?> target);

    @NotNull
    protected ConvertibleMap lastConvertibleField(@NotNull ConvertibleMap convertibleMap) {
        ConvertibleMap currentMap = convertibleMap;
        while (currentMap.hasNext()) {
            currentMap = currentMap.next();
        }
        return currentMap;
    }

    protected Object convertValue(Object value, ConvertibleMap currentMap) {
        while (true) {
            if (value != null || !currentMap.isRetainNull()) {
                boolean isPush = false;
                if (value != null
                        && currentMap.getCollectionElementClass() != null
                        && currentMap.getCollectionElementClass() != Void.class
                        && BeanMapping.hasMapping()) {
                    BeanMapping.push(value.getClass(), currentMap.getCollectionElementClass());
                    isPush = true;
                }

                try {
                    Converter converter = currentMap.getConverter();
                    if (converter == null) {
                        converter = this.converterFilter.filter(value);
                    }

                    if (converter != null) {
                        if (Converter.isTipHasMessage(currentMap.getTip())) {
                            value = converter.convert(value, currentMap.getTip());
                        } else {
                            value = converter.convert(value);
                        }
                    }
                } finally {
                    if (isPush) {
                        BeanMapping.pop();
                    }
                }
            }

            if (currentMap.hasNext()) {
                currentMap = currentMap.next();
            } else {
                break;
            }
        }

        return value;
    }
}
