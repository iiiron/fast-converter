package net.noboard.fastconverter;

public class VerifyResult<K> extends VerifyInfo {
    private K value;

    public VerifyResult(K value, int errCode, String errMessage) {
        super(errCode, errMessage);
        this.value = value;
    }

    public VerifyResult(K value) {
        super();
        this.value = value;
    }

    public VerifyResult(K value, String errMessage) {
        super(errMessage);
        this.value = value;
    }

    public K getValue() {
        return value;
    }
}
