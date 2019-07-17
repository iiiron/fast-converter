package net.noboard.bean;

import net.noboard.fastconverter.Validator;
import net.noboard.fastconverter.VerifyInfo;

public class AgeVarify implements Validator {
    @Override
    public VerifyInfo validate(Object value) {
        if ((Integer)value < 18) {
            return new VerifyInfo("未满18周岁");
        } else {
            return new VerifyInfo();
        }
    }
}
