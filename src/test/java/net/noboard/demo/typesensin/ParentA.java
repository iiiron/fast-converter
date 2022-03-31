package net.noboard.demo.typesensin;

import lombok.Data;
import net.noboard.Sex;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;

import java.util.Date;
import java.util.List;
import java.util.Map;

@ConvertibleBean(relevantClass = ParentB.class)
@Data
public class ParentA {

    private Sex sex;

    private Date birthday;

    private Date birthdayFormat;

    private Long age;

    @ConvertibleField(aliasName = "nickname")
    private String name;

    private List<ChildB> childList;

    private List<Map<Integer, ChildA>> childMap;

    private ChildA child;
}
