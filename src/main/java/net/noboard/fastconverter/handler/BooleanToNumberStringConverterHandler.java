package net.noboard.fastconverter.handler;


import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.core.AbstractConverterHandler;

/**
 * @author wanxm
 */
public class BooleanToNumberStringConverterHandler extends AbstractConverterHandler<Boolean, String> {
    @Override
    protected String converting(Boolean value, String tip) throws ConvertException {
        return value ? "1" : "0";
    }

    @Override
    public boolean supports(Object value, String tip) {
        return value != null && Boolean.class.isAssignableFrom(value.getClass());
    }
}
