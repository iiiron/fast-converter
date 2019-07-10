package net.noboard.beantobeantest;

import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;

import java.util.Date;

public class Man {
    private String name;

    @FieldConverter(converter = DateToFormatStringConverterHandler.class)
    private Date birthday;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
