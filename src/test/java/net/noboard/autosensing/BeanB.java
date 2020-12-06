package net.noboard.autosensing;

import lombok.Data;
import net.noboard.fastconverter.ConvertibleBean;

@Data
@ConvertibleBean(targetClass = BeanA.class)
public class BeanB {
    private Human human;

    private String name;
}
