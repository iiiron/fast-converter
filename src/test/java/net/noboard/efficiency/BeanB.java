package net.noboard.efficiency;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BeanB {
    private String name;

    private String birthday;

    private List<Duct> ducts;

    private Integer age;
}
