package net.noboard.expression;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleBeanType;
import net.noboard.fastconverter.ConvertibleField;

@Data
@ConvertibleBean(relevantClass = BeanB.class, type = ConvertibleBeanType.SOURCE)
public class BeanA {
    @ConvertibleField(converter = Null2EnumFirstConstantConvertHandler.class, context = "T(net.noboard.expression.Sex)", ignoreNull = false)
    private Sex sex;
}
