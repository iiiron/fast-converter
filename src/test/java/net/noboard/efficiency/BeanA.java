package net.noboard.efficiency;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;

import java.util.Date;
import java.util.List;

@Data
@ConvertibleBean(targetClass = BeanB.class)
public class BeanA {
    private String name;

    @ConvertibleField(converter = DateToFormatStringConverterHandler.class)
    private Date birthday;

    @ConvertibleField(nameTo = "ducts")
    @ConvertibleField(nameTo = "ducts", group = "b")
    private List<Son> sons;

    private Integer age;
}
