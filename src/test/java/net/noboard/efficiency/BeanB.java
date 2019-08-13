package net.noboard.efficiency;

import lombok.Data;

import java.util.List;

@Data
public class BeanB {
    private String name;

    private String birthday;

    private List<Daughter> daughters;

    private Integer age;
}
