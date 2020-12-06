package net.noboard.finalTest;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BeanB {
    private String name;

    private String gender;

    private String birthday;

    private List<BeanC> beanCList;
}
