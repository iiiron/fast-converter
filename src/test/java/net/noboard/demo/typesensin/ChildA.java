package net.noboard.demo.typesensin;

import lombok.Data;
import net.noboard.Motion;
import net.noboard.fastconverter.ConvertibleBean;

import java.util.List;

@ConvertibleBean(relevantClass = ChildB.class)
@Data
public class ChildA {

    private String name;

    private List<Motion> hobby;
}
