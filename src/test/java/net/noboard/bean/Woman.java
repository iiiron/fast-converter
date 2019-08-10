package net.noboard.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class Woman {

    private String name;

    private List<List<ChildB>> child;

    private BigDecimal sex;

    private Date birthday;

    private Integer age;

    private Integer[] bbb;

    private Boolean fff;

    private Set<String> ccc;

    private List<String>[] ddd;

    private Map<String, Date> eee;
}
