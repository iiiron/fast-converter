package net.noboard.sourcelab;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class BeanA {
    private Integer age;

    private List<BeanC> name;

    private BeanC tag;

    private List<String> stringList;
}
