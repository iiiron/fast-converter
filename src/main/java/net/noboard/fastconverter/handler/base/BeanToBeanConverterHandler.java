package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.filter.AbstractConverterFilter;
import net.noboard.fastconverter.handler.ArrayToListConverterHandler;
import net.noboard.fastconverter.handler.CollectionToListConverterHandler;
import net.noboard.fastconverter.handler.support.FieldConverterHandler;
import net.noboard.fastconverter.handler.support.PutConverterHandler;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 将指定Bean对象转换为一个新的指定类型的Bean（深度复制）
 * <p>
 * 下面描述中，将待转换Bean叫做A，转换结果叫做B。
 * <p>
 * 该转换器并不要求A和B是相同的类型，由于转换器基于Bean中域的名字（非大小写敏感）来进行数据的映射，
 * 所以A,B不需要是相同的类型，A,B中可以有任意的域具有相同的名字，一旦转换器发现相同域名，就会针对
 * 该域进行A->B的转换，转换过程可通过@FieldConverter注解干预，也可根据注册到ConverterFilter中的
 * 默认转换器来进行默认转换（域名相同，但未通过@FieldConverter指定转换器的域，会被默认转换器转换）。
 */
public class BeanToBeanConverterHandler<T, K> extends AbstractFilterBaseConverterHandler<T, K> {

    private static FieldConverterHandler fieldConverterHandler = new FieldConverterHandler();

    private static BeanToBeanConverterHandler beanCopy = null;

    private Class from = null;

    /**
     * 一个专门用于复制Bean的BeanToBeanConverterHandler实例，它不会对数据做任何修改，除非
     * 你通过@FieldConverter指定了转换器。由于该转换器使用了无参的构造函数实例化，它将无法
     * 处理方法convert(Object)，你必须使用该方法的其他重载方法，明确指出转换的目标类型才可以。
     */
    public static BeanToBeanConverterHandler beanCopy() {
        if (beanCopy == null) {
            beanCopy = new BeanToBeanConverterHandler(new AbstractConverterFilter() {
                @Override
                protected void initConverters(List<Converter<?, ?>> converters) {
                    converters.add(new NullConverterHandler());
                    converters.add(new SkippingConverterHandler(SkippingConverterHandler.BASIC_DATA_TYPE));
                    converters.add(new SkippingConverterHandler(SkippingConverterHandler.COMMON_DATA_TYPE));
                    converters.add(new MapToMapConverterHandler<>(this));
                    converters.add(new ArrayToArrayConverterHandler<>(this));
                    converters.add(new CollectionToCollectionConverterHandler<>(this));
                    converters.add(new BeanToBeanConverterHandler(this));
                }
            });
        }

        return beanCopy;
    }

    private BeanToBeanConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter, String tip) {
        super(converterFilter, tip);
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter, Class to) {
        super(converterFilter, to.getName());
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter, Class from, Class to) {
        super(converterFilter, to.getName());
        this.from = from;
    }

    public BeanToBeanConverterHandler(PutConverterHandler putConverterHandler) {
        this(new AbstractConverterFilter() {
            @Override
            protected void initConverters(List<Converter<?, ?>> converters) {
                putConverterHandler.put(converters);
                converters.add(new BeanToBeanConverterHandler(this));
            }
        });
    }

    @Override
    public boolean supports(Object value) {
        if (this.from != null) {
            return value != null && value.getClass() == this.from;
        } else {
            return value != null;
        }
    }

    public K convert(T value, Class<K> clazz) {
        return this.convert(value, clazz.getName());
    }

    @Override
    public K convert(T value) throws ConvertException {
        if ("".equals(getDefaultTip())) {
            throw new ConvertException("缺少必要的目标类型信息。请检查构造器参数，或使用带tip参数的convert方法。");
        }
        return super.convert(value);
    }

    @Override
    protected K converting(T value, String tip) throws ConvertException {
        BeanInfo beanF, beanT;
        T objF;
        K objT;
        try {
            objF = value;
            objT = (K) Class.forName(tip).newInstance();
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
                                try {
                                    tD.getWriteMethod().invoke(objT, fieldConverterHandler.handler(fieldConverter, r));
                                } catch (IllegalArgumentException e) {
                                    throw new ConvertException("数据转换后类型不符合beanTo对应域的类型。  域:"
                                            + tD.getName() + "  目标类型:" + tD.getPropertyType().getName()
                                            + "  转换器:" + fieldConverter.converter().getName());
                                }
                            }
                        } else {
                            Converter converter = this.getConverter(r);
                            try {
                                if (converter == null) {
                                    throw new ConvertException("没有转换器可以处理数据类型：" + r.getClass().getName());
                                } else if (BeanToBeanConverterHandler.class.isAssignableFrom(converter.getClass())) {
                                    tD.getWriteMethod().invoke(objT, converter.convert(r, tD.getPropertyType().getName()));
                                } else {
                                    tD.getWriteMethod().invoke(objT, converter.convert(r));
                                }
                            } catch (ConvertException | IllegalArgumentException e) {
                                throw new ConvertException("域:" + fD.getName() + "  类型:" + fD.getPropertyType().getName()
                                        + "  转换器类型:" + converter.getClass().getName() + "  " + e.getMessage());
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new ConvertException(e);
                    } catch (NoSuchFieldException e) {
                        throw new ConvertException("");
                    }
                    break;
                }
            }
        }

        return objT;
    }
}
