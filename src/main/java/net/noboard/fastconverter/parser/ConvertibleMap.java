package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.Converter;

import javax.validation.constraints.NotNull;

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

    void setCollectionElementClass(Class<?> clazz);

    Class<?> getCollectionElementClass();

    @NotNull
    ConvertibleMap next();

    static boolean hasConverter(ConvertibleMap cMap) {
        ConvertibleMap current = cMap;
        while (true) {
            if (current == null) {
                return false;
            }
            if (current.getConverter() != null) {
                return true;
            }
            current = current.next();
        }
    }

    static ConvertibleMap last(@NotNull ConvertibleMap cMap) {
        ConvertibleMap currentMap = cMap;
        while (currentMap.hasNext()) {
            currentMap = currentMap.next();
        }
        return currentMap;
    }
}
