package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.core.AbstractConverterHandler;

/**
 * null值转换
 *
 * 该转换器仅接收空值作为参数。它的存在是为了填充转换器过滤器对数据类型（null）的支持。
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

    /**
     *
     * @param value
     * @param tip tip = 1 返回空字符串； tip = 2 返回null
     * @return
     */
    @Override
    protected String converting(Object value, String tip) {
        if ("1".equals(tip)) {
            return "";
        } else if ("2".equals(tip)) {
            return null;
        } else {
            throw new ConvertException(String.format("not support value of tip '%s' of NullConverterHandler", tip));
        }
    }

    @Override
    public boolean supports(Object value, String tip) {
        return value == null;
    }
}
