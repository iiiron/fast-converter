package net.noboard.fastconverter;


import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;

/**
 * @author wanxm
 * @param <T>
 * @param <K>
 */
public abstract class AbstractConverterHandler<T, K> implements Converter<T, K> {
    private String defaultTip;

    public AbstractConverterHandler() {
        this.defaultTip = "";
    }

    public AbstractConverterHandler(String defaultTip) {
        this.defaultTip = defaultTip;
    }

    protected abstract K converting(T value, String tip) throws ConvertException;

    public String getDefaultTip() {
        return this.defaultTip;
    }

    @Override
    public K convert(T value) throws ConvertException {
        return this.convert(value, this.defaultTip);
    }

    @Override
    public K convert(T value, String tip) throws ConvertException {
        if (!this.supports(value, tip)) {
            throw new ConvertException(this.getClass().getName() + " can't convert value: " + value);
        }

        return this.converting(value, tip);
    }

    @Override
    public void setDefaultTip(String tip) {
        this.defaultTip = tip;
    }

    @Override
    public boolean supports(Object value) {
        return supports(value, defaultTip);
    }
}
