package net.noboard.chain;

import net.noboard.fastconverter.AbstractConverterHandler;
import net.noboard.fastconverter.ConvertException;

import java.util.Date;

public class LongToDateConverter extends AbstractConverterHandler<Long, Date> {
    @Override
    protected Date converting(Long value, String tip) throws ConvertException {
        return new Date(value);
    }

    @Override
    public boolean supports(Object value, String tip) {
        return true;
    }
}
