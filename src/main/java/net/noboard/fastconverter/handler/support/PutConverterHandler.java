package net.noboard.fastconverter.handler.support;

import net.noboard.fastconverter.Converter;

import java.util.List;

public interface PutConverterHandler {
    void put(List<Converter<?, ?>> list);
}
