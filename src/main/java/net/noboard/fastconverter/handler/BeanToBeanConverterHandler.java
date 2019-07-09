package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.handler.support.FieldConverterHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 该转换器将Bean T，转换成Bean K。转换针对T和K都存在的域进行，并忽略域的大小写
 */
public class BeanToBeanConverterHandler extends AbstractFilterBaseConverterHandler<BeanConverterContainer, BeanConverterContainer> {

    private FieldConverterHandler fieldConverterHandler = new FieldConverterHandler();

    public BeanToBeanConverterHandler(ConverterFilter converterFilter) {
        super(converterFilter, "positive");
    }

    public BeanToBeanConverterHandler(ConverterFilter converterFilter, String tip) {
        super(converterFilter, tip);
    }

    @Override
    public boolean supports(Object value) {
        return value != null;
    }

    @Override
    protected BeanConverterContainer converting(BeanConverterContainer value, String tip) throws ConvertException {
        Field[] fieldF = value.getBeanFrom().getClass().getDeclaredFields();
        Field[] fieldT = value.getBeanTo().getClass().getDeclaredFields();

        for (Field fF : fieldF) {
            for (Field fT : fieldT) {
                System.out.println(fF.getName() + fT.getName());
                if (fF.getName().toLowerCase().equals(fT.getName().toLowerCase())) {
                    if (tip.toLowerCase().equals("positive")) {
                        convert(value, fF, fT);
                    } else if (tip.toLowerCase().equals("negative")) {
                        convert(value, fT, fF);
                    } else {
                        throw new ConvertException("未指定tip，tip的值可为：positive 或 negative");
                    }
                }
            }
        }
        return value;
    }

    private void convert(BeanConverterContainer value, Field fF, Field fT) {
        FieldConverter annotation = fF.getAnnotation(FieldConverter.class);
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fF.getName(), value.getBeanFrom().getClass());
            Method method = propertyDescriptor.getReadMethod();
            Object readV = method.invoke(value.getBeanFrom());
            propertyDescriptor = new PropertyDescriptor(fT.getName(), value.getBeanTo().getClass());
            method = propertyDescriptor.getWriteMethod();
            if (annotation != null) {
                if (readV != null) {
                    method.invoke(value.getBeanTo(), fieldConverterHandler.handler(annotation, readV));
                }
            } else {
                method.invoke(value.getBeanTo(), this.getConverter(readV).convert(readV));
            }
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            throw new ConvertException(e);
        }
    }
}
