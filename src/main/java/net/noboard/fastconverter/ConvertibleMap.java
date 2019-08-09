package net.noboard.fastconverter;

/**
 * tip：用于转换的tip
 * group：用于转换的group
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
