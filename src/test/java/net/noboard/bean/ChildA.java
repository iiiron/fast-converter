package net.noboard.bean;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;

import java.util.Date;

/**
 * autoConvert(childB, ChildA.class);
 * autoConvert(childA)
 *
 * 源是A
 * - sourceConverter=用A的注解转换出B
 * - targetConverter=用B的注解转换出B
 *
 * group = default, base = ChildA , target = ChildB
 * group = default, base = ChildA , target = ChildC
 *
 * group = default, base = ChildB , target = ChildA
 *
 * group = default, source = ChildA , target = ChildB
 *
 * group = default, source = ChildA , target = ChildC
 *
 */
@Data
@ConvertibleBean(targetClass = ChildB.class)
public class ChildA {
    private String name;

    @ConvertibleField(converter = DateToFormatStringConverterHandler.class, tip = "yyyy-MM-dd HH:mm:ss", nameTo = "birthday")
    private Date birthday;
}
