package net.noboard.fastconverter.handler;

/**
 * 将null转换为空字符串
 *
 * @author wanxm
 */
public class NullToEmptyStringConverterHandler extends AbstractConverterHandler<Object, String> {
    @Override
    protected String converting(Object value, String tip) {
        return "";
    }

    @Override
    public boolean supports(Object value) {
        return value == null;
    }
}
