package net.noboard.fastconverter;

public class VerifyInfo {
    private int errCode = 0;

    private String errMessage = null;

    private boolean isPass;

    public VerifyInfo() {
        this.isPass = true;
    }

    public VerifyInfo(String errMessage) {
        this.errMessage = errMessage;
        this.isPass = false;
    }

    public VerifyInfo(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
        this.isPass = false;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public boolean isPass() {
        return isPass;
    }
}
