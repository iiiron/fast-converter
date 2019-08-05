package net.noboard.fastconverter;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

public class ConvertResult<K> {
    private K value;

    private Set<ConstraintViolation> set;

    public ConvertResult(K value) {
        this.value = value;
    }

    public ConvertResult(K value, Set<ConstraintViolation> set) {
        this.value = value;
        this.set = set;
    }

    public K getValue() {
        return value;
    }

    public void setValue(K value) {
        this.value = value;
    }

    public Set<ConstraintViolation> getSet() {
        return set;
    }

    public void setSet(Set<ConstraintViolation> set) {
        this.set = set;
    }

    public void appendViolation(ConstraintViolation constraintViolation) {
        if (this.set == null) {
            this.set = new HashSet<>();
        }
        this.set.add(constraintViolation);
    }
}
