package net.noboard.bean;

import net.noboard.fastconverter.FieldValidator;
import net.noboard.fastconverter.VerifyInfo;

public class AgeVarify2 implements FieldValidator {
    @Override
    public VerifyInfo validate(Object value, Object context) {
        if ((Integer)value > 60) {
            return new VerifyInfo("超过60周岁");
        } else {
            return new VerifyInfo();
        }
    }

    @Override
    public VerifyInfo validate(Object value) {
        return null;
    }
}
