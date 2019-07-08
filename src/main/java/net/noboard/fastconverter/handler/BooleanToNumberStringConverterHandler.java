package net.noboard.fastconverter.handler;


import net.noboard.fastconverter.ConvertException;

/**
 * @author wanxm
 */
public class BooleanToNumberStringConverterHandler extends AbstractConverterHandler<Boolean, String> {
    @Override
    protected String converting(Boolean value, String tip) throws ConvertException {
        return value ? "1" : "0";
    }

    @Override
    public boolean supports(Object value) {
        return value != null && value.getClass().isAssignableFrom(Boolean.class);
    }
}
