package net.noboard.fastconverter.handler;

/**
 * @author wanxm
 */
public class StringConverterHandler extends AbstractConverterHandler<String, String> {
    @Override
    protected String converting(String value, String tip) {
        return value;
    }

    @Override
    public boolean supports(Object value) {
        return value != null && value.getClass().isAssignableFrom(String.class);
    }
}
