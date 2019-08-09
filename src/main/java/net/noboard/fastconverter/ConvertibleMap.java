package net.noboard.fastconverter;

import java.util.Iterator;

public interface ConvertibleMap {
    void setTip(String tip);

    String getTip();

    void setGroup(String group);

    String getGroup();

    void setConverter(Converter converter);

    Converter getConverter();

    void join(ConvertibleMap convertibleMap);

    boolean hasNext();

    ConvertibleMap next();
}
