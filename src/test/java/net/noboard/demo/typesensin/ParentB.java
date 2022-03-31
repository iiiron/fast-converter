package net.noboard.demo.typesensin;

import lombok.Data;
import net.noboard.Sex;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ParentB {

    private String sex;

    private Long birthday;

    private String birthdayFormat;

    private Long age;

    private String nickname;

    private List<ChildA> childList;

    private List<Map<String, ChildB>> childMap;

    private ChildB child;
}
