package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.Converter;

/**
 * tip：用于转换的tip
 * converter：转换器
 */
public interface ConvertibleMap {
    void setAbandon(boolean abandon);

    boolean isAbandon();

    void setRetainNull(boolean isRetainNull);

    boolean isRetainNull();

    void setTip(String tip);

    String getTip();

    void setNameTo(String nameTo);

    String getNameTo();

    void setConverter(Converter converter);

    Converter getConverter();

    void join(ConvertibleMap convertibleMap);

    boolean hasNext();

    ConvertibleMap next();
}
