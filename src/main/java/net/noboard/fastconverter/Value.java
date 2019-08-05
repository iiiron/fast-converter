package net.noboard.fastconverter;

public class Value extends VerifyInfo {
    private Object value;

    private String tip;

    public Value() {
        super();
    }

    public Value(Object value) {
        super();
        this.value = value;
    }

    public Value(String errMessage, Object value) {
        super(errMessage);
        this.value = value;
    }

    public Value(Integer errCode, String errMessage, Object value) {
        super(errCode, errMessage);
        this.value = value;
    }

    public Value(String flag, String errMessage, Object value) {
        super(flag, errMessage);
        this.value = value;
    }

    public Value(Integer errCode, String flag, String errMessage, Object value) {
        super(errCode, flag, errMessage);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
