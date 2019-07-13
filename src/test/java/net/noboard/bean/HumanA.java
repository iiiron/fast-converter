package net.noboard.bean;

import lombok.Data;

import java.util.List;

@Data
public class HumanA {

    private Man human;

    private List<ChildA> children;
}
