package net.noboard.enumConverter;

import lombok.Data;
import net.noboard.expression.Null2EnumFirstConstantConvertHandler;
import net.noboard.Sex;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleBeanType;
import net.noboard.fastconverter.ConvertibleField;

@Data
public class BeanA {
    private Sex sex;
}
