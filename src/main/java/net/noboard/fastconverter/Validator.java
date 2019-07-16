package net.noboard.fastconverter;

public interface Validator {
    boolean validate(Object field, Object bean);

    VerifyInfo verifyInfo();
}
