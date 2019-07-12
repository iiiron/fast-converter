package net.noboard.beantobeantest;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class Woman {

    private String name;

    private String birthday;

    private String sex;

    private int age;

    private int[] aaa;

    private Integer[] bbb;

    private Set ccc;

    private List<String>[] ddd;

    private Map<String, Date> eee;

    private BigDecimal fff;
}
