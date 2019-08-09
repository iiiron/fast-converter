package net.noboard.fastconverter;

public interface ConvertibleMap {
    void setTip(String tip);

    String getTip();

    void setGroup(String group);

    String getGroup();

    void setConverter(Converter converter);

    Converter getConverter();
}
