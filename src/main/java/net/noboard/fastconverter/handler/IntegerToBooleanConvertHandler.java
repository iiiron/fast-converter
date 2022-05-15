package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.AbstractConverterHandler;

/**
 * 大于0则为true, 否则为false
 *
 * @date 2022/5/15 20:15
 * @author by wanxm
 */
public class IntegerToBooleanConvertHandler extends AbstractConverterHandler<Integer, Boolean, Void> {
    @Override
    protected Boolean doConvert(Integer value, Void context) {
        return value > 0;
    }

    @Override
    protected Void defaultContext() {
        return null;
    }
}
