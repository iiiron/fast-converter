package net.noboard.fastconverter;

import net.noboard.fastconverter.handler.bean.ConvertInfo;
import net.noboard.fastconverter.parser.ConvertibleMap;

import javax.validation.constraints.NotNull;

public class CMap implements ConvertibleMap {

    private String tip;

    private Converter converter;

    private ConvertibleMap next;

    private boolean abandon;

    private boolean skipNull;

    private boolean hasNext = false;

    private String aliasName;

    private Class<?> relevantClass;

    private Object convertInfo;

    @Override
    public Class<?> getRelevantClass() {
        return relevantClass;
    }

    @Override
    public void setRelevantClass(Class<?> collectionElementClass) {
        this.relevantClass = collectionElementClass;
    }

    @Override
    public String getAliasName() {
        return aliasName;
    }

    @Override
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @Override
    public String getTip() {
        return tip;
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
    public void setRetainNull(boolean isSkipNull) {
        this.skipNull = isSkipNull;
    }

    @Override
    public boolean isRetainNull() {
        return this.skipNull;
    }

    @Override
    public void setConvertContext(Object context) {
        this.convertInfo = context;
    }

    @Override
    public Object getConvertContext() {
        return this.convertInfo;
    }

    @Override
    public void setTip(String tip) {
        this.tip = tip;
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
                "tip='" + tip + '\'' +
                ", converter=" + converter +
                ", next=" + next +
                ", abandon=" + abandon +
                ", skipNull=" + skipNull +
                ", hasNext=" + hasNext +
                ", aliasName='" + aliasName + '\'' +
                ", relevantClass=" + relevantClass +
                '}';
    }
}
