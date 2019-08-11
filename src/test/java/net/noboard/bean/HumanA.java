package net.noboard.bean;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;

import java.util.List;
import java.util.Map;

@Data
@ConvertibleBean(targetClass = Map.class)
public class HumanA {

    private Man human;

    private List<ChildA> children;
}
