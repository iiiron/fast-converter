package net.noboard.efficiency;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;

import java.util.Date;

@Data
@ConvertibleBean(targetClass = Duct.class)
public class Son {
    private String name;

    @ConvertibleField(converter = DateToFormatStringConverterHandler.class, tip = "yyyy.MM")
    private Date birthday;
}
