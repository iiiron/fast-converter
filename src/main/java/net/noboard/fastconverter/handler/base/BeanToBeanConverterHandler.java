package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.filter.CommonSkipConverterFilter;
import net.noboard.fastconverter.handler.support.ConverterExceptionHelper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 可以将一个类型的Bean转换为另一个类型的Bean，Bean中的Bean可以自适应转换
 * <p>
 * <p>
 * 将指定Bean对象转换为一个新类型的Bean
 * <p>
 * ConverterFilter不支持的域将照搬原值。如果不指定转换目标类型，则转换目标类型=被转换Bean的类型（转换过程
 * 会按照转换器一一进行）。
 *
 * <p>
 * 下面描述中，将待转换Bean叫做A，转换结果叫做B。
 * <p>
 * 该转换器并不要求A和B是相同的类型，由于转换器基于Bean中域的名字（非大小写敏感）来进行数据的映射，
 * 所以A,B不需要是相同的类型，转换器仅针对A，B中相同域名的域进行A->B的转换，转换过程可通过@ConverterIndicator
 * 注解干预，也可根据注册到ConverterFilter中的默认转换器来进行默认转换（域名相同，但未通过@ConverterIndicator
 * 指定转换器的域，会被默认转换器转换）。
 * <p>
 * 如果不指定转换结果类型，则B的类型将等同A的类型。在这种情况下不能将BeanToBeanConverterHandler的行为
 * 理解为浅复制或者深复制。BeanToBeanConverterHandler的行为受到ConverterFilter中注册的各种转换器的
 * 实现的影响。而这些转换器并不一定会在转换后返回新的对象（例如SkippingConverterHandler）。
 */
public class BeanToBeanConverterHandler<T, K> extends AbstractBeanConverterHandler<T, K> {

    private static BeanToBeanConverterHandler beanTransfer = null;

    private Class<T> from = null;

    /**
     * 一个专门用于递归复制Bean的BeanToBeanConverterHandler实例，它不会对数据做任何修改，除非
     * 你通过@FieldConverter指定了转换器。
     */
    public static BeanToBeanConverterHandler transfer() {
        if (beanTransfer == null) {
            ConverterFilter converterFilter = new CommonSkipConverterFilter();
            beanTransfer = new BeanToBeanConverterHandler(converterFilter);
            converterFilter.addLast(beanTransfer);
        }

        return beanTransfer;
    }

    /**
     * transfer的自定义版本，用户可以通过传递不同的ConverterFilter实例，来影响默认转换行为。
     * 需要注意的是，它会自动在转换链末尾添加BeanToBeanConverterHandler转换器（以实现递归的
     * Bean To Bean转换能力），所以你不必在ConverterFilter实例中手动添加。
     *
     * @param converterFilter
     * @return
     */
    public static BeanToBeanConverterHandler transferCustom(ConverterFilter converterFilter) {
        BeanToBeanConverterHandler converterHandler = new BeanToBeanConverterHandler(converterFilter);
        converterFilter.addLast(converterHandler);
        return converterHandler;
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter);
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter, String tip) {
        super(converterFilter, tip);
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter, Class to) {
        super(converterFilter, to.getName());
    }

    /**
     * 该构造函数同其他的区别在于，它接收一个标识beanFrom的类型的class对象参数，使用该
     * 构造函数实例化的BeanToBean转换器，会在验证中检查beanFrom的类型是否是该构造器
     * 参数类型的子类，本类或接口实现类，是则验证通过，否则验证不通过。
     *
     * @param converterFilter
     * @param from
     * @param to
     */
    public BeanToBeanConverterHandler(ConverterFilter converterFilter, Class<T> from, Class<K> to) {
        super(converterFilter, to.getName());
        this.from = from;
    }

    @Override
    public boolean supports(Object value) {
        if (this.from != null) {
            return value != null && value.getClass() == this.from;
        } else {
            return value != null;
        }
    }

    public K convert(T value, Class clazz) {
        return this.convert(value, clazz.getName());
    }

    @Override
    protected K converting(T value, String tip) throws ConvertException {
        return this.convertingAndVerify(value, tip, null).getValue();
    }

    /**
     * convert函数的tip > Bean ConverterIndicator的tip > 转换为自身类型
     *
     * @param value
     * @param tip
     * @param afterConvert
     * @return
     * @throws ConvertException
     */
    @Override
    protected VerifyResult<K> convertingAndVerify(T value, String tip, Validator afterConvert) throws ConvertException {
        BeanInfo beanF, beanT;
        Object objF = value, objT;
        BeanConverterIndicator converterIndicator = value.getClass().getAnnotation(BeanConverterIndicator.class);
        if (converterIndicator != null) {
            if ((tip == null || "".equals(tip)) && !converterIndicator.targetType().equals(Void.class)) {
                tip = defaultTip(converterIndicator.targetType().getName());
            }
            if (afterConvert == null && !converterIndicator.afterConvert().isInterface()) {
                try {
                    afterConvert = converterIndicator.afterConvert().newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new ConvertException("实例化" + converterIndicator.afterConvert() + "失败", e);
                }
            }
        }

        try {
            objT = (tip == null || "".equals(tip)) ? objF.getClass().newInstance() : Class.forName(tip).newInstance();
            beanF = Introspector.getBeanInfo(objF.getClass());
            beanT = Introspector.getBeanInfo(objT.getClass());
        } catch (Exception e) {
            throw new ConvertException("实例化Bean失败", e);
        }

        StringBuilder stringBuilder = new StringBuilder();
        Map<String, VerifyInfo> errMap = new HashMap<>();
        for (PropertyDescriptor fD : beanF.getPropertyDescriptors()) {
            if ("class".equals(fD.getName())) {
                continue;
            }

            for (PropertyDescriptor tD : beanT.getPropertyDescriptors()) {
                if (fD.getName().toLowerCase().equals(tD.getName().toLowerCase())) {
                    try {
                        Field field = objF.getClass().getDeclaredField(fD.getName());
                        ConverterIndicator[] converterIndicators = field.getAnnotationsByType(ConverterIndicator.class);
                        Object fieldOriginalValue = fD.getReadMethod().invoke(objF);
                        Object afterConvertValue = fieldOriginalValue;
                        Converter converter = null;
                        if (converterIndicators != null && converterIndicators.length > 0) {
                            // 指定了转换器
                            if (fieldOriginalValue != null) {
                                // 原值不为null，（原值为null则跳过）
                                for (ConverterIndicator ci : converterIndicators) {
                                    converter = this.getConverter(ci);
                                    Validator validator = this.getValidator(ci);
                                    VerifyResult verifyResult;
                                    if ("".equals(ci.tip())) {
                                        verifyResult = converter.convertAndVerify(afterConvertValue, validator);
                                    } else {
                                        verifyResult = converter.convertAndVerify(afterConvertValue, ci.tip(), validator);
                                    }
                                    afterConvertValue = verifyResult.getValue();
                                    if (!verifyResult.isPass()) {
                                        putErrMessage(stringBuilder, tD.getName(), verifyResult.getErrMessage());
                                    }
                                }
                            }
                        } else {
                            // 没有指定转换器，从转换器过滤器中筛选合适的转换器
                            converter = this.filter(fieldOriginalValue);
                            if (converter != null) {
                                VerifyResult verifyResult;
                                if (this.getClass().equals(converter.getClass())) {
                                    // 这里是为了赋予BeanToBeanConverter自动转换Bean内部的Bean
                                    verifyResult = converter.convertAndVerify(fieldOriginalValue, tD.getPropertyType().getName());
                                } else {
                                    verifyResult = converter.convertAndVerify(fieldOriginalValue);
                                }

                                afterConvertValue = verifyResult.getValue();
                                if (!verifyResult.isPass()) {
                                    putErrMessage(stringBuilder, tD.getName(), verifyResult.getErrMessage());
                                }
                            }
                        }
                        try {
                            tD.getWriteMethod().invoke(objT, afterConvertValue);
                        } catch (IllegalArgumentException e) {
                            ConverterExceptionHelper.factory(objF, fieldOriginalValue, objT, afterConvertValue, converter, e);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new ConvertException(e);
                    } catch (NoSuchFieldException e) {
                        throw new ConvertException(
                                MessageFormat.format("BeanToBeanConverterHandler反射获取对象域时发生异常：NoSuchFieldException  {0}.{1}  通常是因为在转换器过滤器中没有添加对相关类的支持，导致某些非Bean的java类被错误的抛给了BeanToBeanConverterHandler去处理", objF.getClass().getName(), fD.getName()),
                                e);
                    }
                    break;
                }
            }
        }

        if (stringBuilder.length() > 0) {
            stringBuilder.insert(0, " field verify: ");
        }

        VerifyInfo verifyInfo = afterConvert == null ? null : afterConvert.validate(objT);
        if (verifyInfo != null && !verifyInfo.isPass()) {
            stringBuilder.insert(0, " bean verify: " + verifyInfo.getErrMessage() + ". ");
        }

        if (stringBuilder.length() > 0) {
            return new VerifyResult<>((K) objT, stringBuilder.insert(0,"{").append("}").toString());
        } else {
            return new VerifyResult<>((K) objT);
        }
    }

    private void putErrMessage(StringBuilder stringBuilder, String fieldName, String message) {
        stringBuilder.append(fieldName);
        stringBuilder.append(":");
        stringBuilder.append(message).append(" ");
    }
}
