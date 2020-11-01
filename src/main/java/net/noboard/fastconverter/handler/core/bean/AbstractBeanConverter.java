package net.noboard.fastconverter.handler.core.bean;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.parser.ConvertibleMap;
import net.noboard.fastconverter.support.ConvertibleAnnotatedUtils;

import javax.validation.constraints.NotNull;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

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
        Object targetObj;
        try {
            targetObj = BeanMapping.current().getTarget().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new ConvertException(
                    String.format("the target class %s can not be implemented",
                            BeanMapping.current().getTarget()), e);
        }

        Map<String, Object> map = parse(from, group);

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

    abstract protected Map<String, Object> parse(Object source, String group);

    @NotNull
    protected ConvertibleMap lastConvertibleField(@NotNull ConvertibleMap convertibleMap) {
        ConvertibleMap currentMap = convertibleMap;
        while (currentMap.hasNext()) {
            currentMap = currentMap.next();
        }
        return currentMap;
    }

    protected Object getConvertedValue(Object value, ConvertibleMap currentMap) {
        while (true) {
            if (value != null || !currentMap.isRetainNull()) {
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
