package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.Converter;

import javax.validation.constraints.NotNull;

/**
 * tip：用于转换的tip
 * converter：转换器
 */
public interface ConvertibleMap {
    void setAbandon(boolean abandon);

    /**
     * 是否忽略该字段
     * @return
     */
    boolean isAbandon();

    void setIgnoreNull(boolean ignoreNull);

    /**
     * 是否保留空值
     * @return
     */
    boolean ignoreNull();

    void setConvertContext(Object context);

    Object getConvertContext();

    void setAliasName(String aliasName);

    /**
     * 获取该字段的别名
     * @return
     */
    String getAliasName();

    void setConverter(Converter converter);

    /**
     * 获取该字段设置的转换器
     * @return
     */
    Converter getConverter();

    void join(ConvertibleMap convertibleMap);

    /**
     * 是否存在下一个ConvertibleMap
     * @return
     */
    boolean hasNext();

    void setRelevantClass(Class<?> clazz);

    /**
     * 获取该字段的关联类型
     * @return
     */
    Class<?> getRelevantClass();

    /**
     * 获取下一个ConvertibleMap
     * @return
     */
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
