package net.noboard.beantobeantest;

import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;

import java.util.Date;

public class Man {
    private String name;

    @FieldConverter(converter = DateToFormatStringConverterHandler.class)
    private Date birthday;

    private Integer age;

    private Boolean sex;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

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
