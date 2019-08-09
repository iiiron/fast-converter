package net.noboard.bean;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.ConvertibleField;
import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.handler.*;
import net.noboard.fastconverter.handler.base.CollectionToCollectionConverterHandler;

import java.util.*;

@Data
//@ConvertibleBean(targetClass = Woman.class)
@ConvertibleBean(targetClass = Woman.class, group = "b")
//@ConvertibleBean(targetClass = ChildA.class, group = "B")
public class Man {
    private String name = "wanxm";

    @ConvertibleField(group = "b", tip = "toChildC")
    List<List<ChildA>> child;
}
