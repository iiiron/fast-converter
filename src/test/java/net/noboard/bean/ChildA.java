package net.noboard.bean;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;

import java.util.Date;

@Data
@ConvertibleBean(targetClass = ChildB.class, tip = "A")
public class ChildA {
    private String name;

    @ConvertibleField(converter = DateToFormatStringConverterHandler.class, group = "A", tip = "yyyy-MM-dd HH:mm:ss", nameTo = "birthday")
    private Date birth;
}
