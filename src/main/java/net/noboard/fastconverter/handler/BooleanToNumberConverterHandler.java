package net.noboard.fastconverter.handler;


import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.core.AbstractConverterHandler;

/**
 * @author wanxm
 */
public class BooleanToNumberConverterHandler extends AbstractConverterHandler<Boolean, Integer> {
    @Override
    protected Integer converting(Boolean value, String tip) throws ConvertException {
        return value ? 1 : 0;
    }

    @Override
    public boolean supports(Object value) {
        return value != null && Boolean.class.isAssignableFrom(value.getClass());
    }
}
