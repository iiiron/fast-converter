package net.noboard.enumConverter;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;

@Data
@ConvertibleBean(relevantClass = BeanA.class)
public class BeanB {

    private String sex;
}
