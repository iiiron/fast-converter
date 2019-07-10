package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.filter.AbstractConverterFilter;
import net.noboard.fastconverter.handler.ArrayToListConverterHandler;
import net.noboard.fastconverter.handler.CollectionToListConverterHandler;
import net.noboard.fastconverter.handler.support.FieldConverterHandler;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 将指定Bean对象转换为一个新的指定类型的Bean（深度复制）
 *
 * 下面描述中，将待转换Bean叫做A，转换结果叫做B。
 *
 * 该转换器并不要求A和B是相同的类型，由于转换器基于Bean中域的名字（非大小写敏感）来进行数据的映射，
 * 所以A,B不需要是相同的类型，A,B中可以有任意的域具有相同的名字，一旦转换器发现相同域名，就会针对
 * 该域进行A->B的转换，转换过程可通过@FieldConverter注解干预，也可根据注册到ConverterFilter中的
 * 默认转换器来进行默认转换（域名相同，但未通过@FieldConverter指定转换器的域，会被默认转换器转换）。
 *
 *
 */
public class BeanToBeanConverterHandler extends AbstractFilterBaseConverterHandler<Object, Object> {

    private static FieldConverterHandler fieldConverterHandler = new FieldConverterHandler();

    private static BeanToBeanConverterHandler beanCopy = null;

    private BeanToBeanConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter, String tip) {
        super(converterFilter, tip);
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter, Class clazz) {
        super(converterFilter, clazz.getName());
    }

    public static BeanToBeanConverterHandler beanCopy() {
        if (beanCopy == null) {
            beanCopy = new BeanToBeanConverterHandler(new AbstractConverterFilter() {
                @Override
                protected void initConverters(List<Converter<?, ?>> converters) {
                    converters.add(new NullConverterHandler());
                    converters.add(new SkippingConverterHandler(SkippingConverterHandler.BASIC_DATA_TYPE));
                    converters.add(new SkippingConverterHandler(SkippingConverterHandler.COMMON_DATA_TYPE));
                    converters.add(new MapToMapConverterHandler<>(this));
                    converters.add(new ArrayToListConverterHandler<>(this));
                    converters.add(new CollectionToListConverterHandler<>(this));
                    converters.add(new BeanToBeanConverterHandler(this));
                }
            });
        }
        return beanCopy;
    }

    @Override
    public boolean supports(Object value) {
        return value != null;
    }

    public Object convert(Object value, Class clazz) {
        return this.convert(value, clazz.getName());
    }

    @Override
    public Object convert(Object value) throws ConvertException {
        if ("".equals(getDefaultTip())) {
            throw new ConvertException("缺少必要的目标类型信息。请检查构造器参数，或使用带tip参数的convert方法。");
        }
        return super.convert(value);
    }

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
        BeanInfo beanF, beanT;
        Object objF, objT;
        try {
            objF = value;
            objT = Class.forName(tip).newInstance();
            beanF = Introspector.getBeanInfo(objF.getClass());
            beanT = Introspector.getBeanInfo(objT.getClass());
        } catch (IntrospectionException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new ConvertException("BeanInfo初始化错误，请检查Bean的申明是否正确" + e);
        }

        for (PropertyDescriptor fD : beanF.getPropertyDescriptors()) {
            if ("class".equals(fD.getName())) {
                continue;
            }

            for (PropertyDescriptor tD : beanT.getPropertyDescriptors()) {
                if (fD.getName().toLowerCase().equals(tD.getName().toLowerCase())) {
                    try {
                        Field field = objF.getClass().getDeclaredField(fD.getName());
                        FieldConverter fieldConverter = field.getAnnotation(FieldConverter.class);
                        Object r = fD.getReadMethod().invoke(objF);
                        if (fieldConverter != null) {
                            if (r != null) {
                                tD.getWriteMethod().invoke(objT, fieldConverterHandler.handler(fieldConverter, r));
                            }
                        } else {
                            Converter converter = this.getConverter(r);
                            if (BeanToBeanConverterHandler.class.isAssignableFrom(converter.getClass())) {
                                tD.getWriteMethod().invoke(objT, converter.convert(r, tD.getPropertyType().getName()));
                            } else {
                                tD.getWriteMethod().invoke(objT, converter.convert(r));
                            }
                        }
                    } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
                        throw new ConvertException(e);
                    }
                }
            }
        }

        return objT;
    }
}
