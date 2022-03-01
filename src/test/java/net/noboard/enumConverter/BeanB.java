package net.noboard.enumConverter;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleBeanType;

@Data
@ConvertibleBean(relevantClass = BeanA.class, type = ConvertibleBeanType.SOURCE)
public class BeanB {

    private String sex;
}
