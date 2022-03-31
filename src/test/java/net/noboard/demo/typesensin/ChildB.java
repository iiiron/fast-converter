package net.noboard.demo.typesensin;

import lombok.Data;
import net.noboard.Motion;
import net.noboard.fastconverter.ConvertibleBean;

import java.util.List;

@Data
@ConvertibleBean(relevantClass = ChildA.class)
public class ChildB {

    private String name;

    private List<String> hobby;
}
