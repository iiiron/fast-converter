package net.noboard.fastconverter;

import net.noboard.fastconverter.parser.ConvertibleMap;

import javax.validation.constraints.NotNull;

/**
 * 转换地图, 转换逻辑会根据这个地图信息进行数据转换
 *
 * @date 2022/4/19 18:11
 * @author by wanxm
 */
public class CMap implements ConvertibleMap {

    /**
     * 向上转换器
     */
    private Converter upCastingConverter;

    /**
     * 向下转换器
     */
    private Converter downCastingConverter;

    /**
     * 转换器
     */
    private Converter converter;

    /**
     * 下一个转换地图
     */
    private ConvertibleMap next;

    /**
     * 是否忽略当前字段
     */
    private boolean abandon = false;

    /**
     * 是否忽略null值, 为true表示不对null值进行转换操作, 将null值直接传递给结果, 默认为true
     */
    private boolean ignoreNull = true;

    /**
     * 是否有下一个
     */
    private boolean hasNext = false;

    private String aliasName;

    private Object convertContext;

    @Override
    public String getAliasName() {
        return aliasName;
    }

    @Override
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @Override
    public void setAbandon(boolean abandon) {
        this.abandon = abandon;
    }

    @Override
    public boolean isAbandon() {
        return this.abandon;
    }

    @Override
    public void setIgnoreNull(boolean ignoreNull) {
        this.ignoreNull = ignoreNull;
    }

    @Override
    public boolean ignoreNull() {
        return this.ignoreNull;
    }

    @Override
    public void setConvertContext(Object context) {
        this.convertContext = context;
    }

    @Override
    public Object getConvertContext() {
        return this.convertContext;
    }

    @Override
    public Converter getConverter() {
        return converter;
    }

    @Override
    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    @Override
    public Converter getUpCastingConverter() {
        return upCastingConverter;
    }

    @Override
    public void setUpCastingConverter(Converter upCastingConverter) {
        this.upCastingConverter = upCastingConverter;
    }

    @Override
    public Converter getDownCastingConverter() {
        return downCastingConverter;
    }

    @Override
    public void setDownCastingConverter(Converter downCastingConverter) {
        this.downCastingConverter = downCastingConverter;
    }

    @Override
    public void join(ConvertibleMap convertibleMap) {
        this.next = convertibleMap;
        this.hasNext = true;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public @NotNull ConvertibleMap next() {
        return this.next;
    }

    @Override
    public String toString() {
        return "CMap{" +
                "converter=" + converter +
                ", next=" + next +
                ", abandon=" + abandon +
                ", ignoreNull=" + ignoreNull +
                ", hasNext=" + hasNext +
                ", aliasName='" + aliasName + '\'' +
                ", convertInfo=" + convertContext +
                '}';
    }
}
