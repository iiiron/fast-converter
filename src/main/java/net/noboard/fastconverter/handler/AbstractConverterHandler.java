package net.noboard.fastconverter.handler;


import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;

/**
 * @author wanxm
 * @param <T>
 * @param <K>
 */
public abstract class AbstractConverterHandler<T, K> implements Converter<T, K> {
    private String tip;

    public AbstractConverterHandler() {
        this.tip = "";
    }

    public AbstractConverterHandler(String tip) {
        this.tip = tip;
    }

    protected abstract K converting(T value, String tip) throws ConvertException;

    @Override
    public K convert(T value) throws ConvertException {
        return this.convert(value, this.tip);
    }

    @Override
    public K convert(T value, String tip) throws ConvertException {
        if (!this.supports(value)) {
            throw new ConvertException(this.getClass().getName() + " 无法转换数据 " + value);
        }

        return this.converting(value, tip);
    }
}
