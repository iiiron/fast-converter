package net.noboard.bean;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;

import java.util.List;

@Data
@ConvertibleBean(targetClass = HumanB.class)
public class HumanA {

    private Man human;

    private List<ChildA> children;
}
