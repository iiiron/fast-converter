package net.noboard.fastconverter;

public class CMap implements ConvertibleMap {

    private String tip;

    private String group;

    private Converter converter;

    private ConvertibleMap next;

    private boolean hasNext = false;

    @Override
    public String getTip() {
        return tip;
    }

    @Override
    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public Converter getConverter() {
        return converter;
    }

    @Override
    public void join(ConvertibleMap convertibleMap) {
        this.next = convertibleMap;
        this.hasNext = true;
    }

    @Override
    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    @Override
    public String toString() {
        return "CMap{" +
                "tip='" + tip + '\'' +
                ", group='" + group + '\'' +
                ", converter=" + converter +
                '}';
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public ConvertibleMap next() {
        return this.next;
    }
}
