package net.noboard.efficiency;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.BooleanToNumberConverterHandler;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;
import net.noboard.fastconverter.handler.NumberToBigDecimalConverterHandler;

import java.util.Date;

@Data
@ConvertibleBean(targetClass = Duct.class)
public class Son {
    private String name;

    @ConvertibleField(converter = DateToFormatStringConverterHandler.class, tip = "yyyy.MM")
    private Date birthday;

    @ConvertibleField(converter = BooleanToNumberConverterHandler.class)
    @ConvertibleField(converter = NumberToBigDecimalConverterHandler.class, nameTo = "sexy")
    private Boolean sex;
}
