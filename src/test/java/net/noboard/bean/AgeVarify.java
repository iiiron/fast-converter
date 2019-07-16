package net.noboard.bean;

import net.noboard.fastconverter.FieldValidator;
import net.noboard.fastconverter.VerifyInfo;

public class AgeVarify implements FieldValidator {
    @Override
    public VerifyInfo validate(Object value, Object context) {
        if ((Integer)value < 18) {
            return new VerifyInfo("未满18周岁");
        } else {
            return new VerifyInfo();
        }
    }

    @Override
    public VerifyInfo validate(Object value) {
        return null;
    }
}
