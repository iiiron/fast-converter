package net.noboard.efficiency;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.BooleanToNumberConverterHandler;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;
import net.noboard.fastconverter.handler.NumberToBigDecimalConverterHandler;

import java.util.Date;
import java.util.Map;

@Data
@ConvertibleBean(targetClass = Daughter.class)
@ConvertibleBean(targetClass = Map.class, group = "toMap")
public class Son {
    @ConvertibleField(converter = AddNumberConverterHandler.class)
    private String name;

    @ConvertibleField(converter = DateToFormatStringConverterHandler.class, tip = "yyyy.MM")
    private Date birthday;

//    @ConvertibleField(converter = BooleanToNumberConverterHandler.class)
    @ConvertibleField(converter = NumberToBigDecimalConverterHandler.class, nameTo = "sexy")
    private Boolean sex;
}
