package net.noboard.fastconverter;

import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.base.BeanToBeanConverterHandler;
import net.noboard.fastconverter.handler.support.ConvertibleUtils;

public class ConvertibleBeanParser extends AbstractConvertibleParser {

    // TODO BeanToMap的支持
    @Override
    public ConvertibleMap parse() {
        ConvertibleBean convertibleBean = ConvertibleUtils.getMergedConvertBean(super.annotatedElement, super.group);

        if (convertibleBean == null) {
            throw new IllegalArgumentException(String.format("the group '%s' must be declared by @ConvertibleBean on %s", super.group, annotatedElement.toString()));
        }

        CMap cMap = new CMap();
        if (convertibleBean.converter() != Converter.class) {
            try {
                cMap.setConverter(convertibleBean.converter().newInstance());
            } catch (IllegalAccessException | InstantiationException e) {
                throw new IllegalArgumentException(String.format("the class %s pointed by attribute 'converter' in @ConvertibleBean can not be implemented", convertibleBean.converter().getName()));
            }
        } else {
            BeanToBeanConverterHandler beanToBeanConverterHandler =
                    new BeanToBeanConverterHandler(new CommonConverterFilter(), convertibleBean.group());
            if (convertibleBean.nested()) {
                beanToBeanConverterHandler.getFilter().addLast(beanToBeanConverterHandler);
            }
            cMap.setConverter(beanToBeanConverterHandler);
        }
        cMap.setTip(convertibleBean.tip());
        cMap.setGroup(group);
        return cMap;
    }
}
