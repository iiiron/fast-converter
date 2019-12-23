package net.noboard.demo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
public class Woman {
    private String birthday;

    private BigDecimal age;

    private BigDecimal sex;

    private String name;
}
