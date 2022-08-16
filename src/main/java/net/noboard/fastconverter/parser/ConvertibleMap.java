package net.noboard.fastconverter.parser;

import net.noboard.fastconverter.Converter;

import javax.validation.constraints.NotNull;

/**
 * 转换地图, 转换逻辑会根据这个地图信息进行数据转换
 *
 * @date 2022/4/19 18:14
 * @author by wanxm
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
     * 是否忽略空值, 单
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

    @Deprecated
    void setConverter(Converter converter);

    /**
     * 获取该字段设置的转换器
     * @return
     */
    @Deprecated
    Converter getConverter();

    void setUpCastingConverter(Converter converter);

    /**
     * 获取该字段设置的向上转换器
     * @return
     */
    Converter getUpCastingConverter();

    void setDownCastingConverter(Converter converter);

    /**
     * 获取该字段设置的向下转换器
     * @return
     */
    Converter getDownCastingConverter();

    void join(ConvertibleMap convertibleMap);

    /**
     * 是否存在下一个ConvertibleMap
     * @return
     */
    boolean hasNext();

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
