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
}
