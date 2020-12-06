package net.noboard.finalTest;

import lombok.Data;

import java.util.Date;

@Data
public class BeanA {
    private String name;

    private GenderEnum gender;

    private Date birthday;
}
