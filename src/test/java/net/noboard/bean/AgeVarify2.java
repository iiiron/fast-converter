package net.noboard.bean;

import net.noboard.fastconverter.Validator;
import net.noboard.fastconverter.VerifyInfo;

public class AgeVarify2 implements Validator {
    @Override
    public VerifyInfo validate(Object value) {
        if ((Integer)value > 60) {
            return new VerifyInfo("超过60周岁");
        } else {
            return new VerifyInfo();
        }
    }
}
