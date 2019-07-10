package net.noboard.fastconverter.handler.base;

import net.noboard.fastconverter.handler.base.AbstractConverterHandler;

/**
 * null值转换
 *
 * tip：1 - 转换为空字符串；2 - 保留null
 *
 * @author wanxm
 */
public class NullConverterHandler extends AbstractConverterHandler<Object, String> {

    public NullConverterHandler() {
        super("2");
    }

    public NullConverterHandler(String tip) {
        super(tip);
    }

    @Override
    protected String converting(Object value, String tip) {
        if ("1".equals(tip)) {
            return "";
        } else if ("2".equals(tip)) {
            return null;
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Object value) {
        return value == null;
    }
}