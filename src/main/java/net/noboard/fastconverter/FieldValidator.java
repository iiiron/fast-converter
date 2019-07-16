package net.noboard.fastconverter;

public interface FieldValidator extends Validator {
    VerifyInfo validate(Object value, Object context);
}
