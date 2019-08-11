package net.noboard.fastconverter;

/**
 * tip：用于转换的tip
 * converter：转换器
 */
public interface ConvertibleMap {
    void setTip(String tip);

    String getTip();

    void setConverter(Converter converter);

    Converter getConverter();

    void join(ConvertibleMap convertibleMap);

    boolean hasNext();

    ConvertibleMap next();
}
