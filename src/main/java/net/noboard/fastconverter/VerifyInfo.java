package net.noboard.fastconverter;

public class VerifyInfo {
    private int errCode;

    private String errMessage;

    public VerifyInfo(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }
}
