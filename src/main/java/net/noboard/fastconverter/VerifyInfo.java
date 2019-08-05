package net.noboard.fastconverter;

import java.util.ArrayList;
import java.util.List;

public class VerifyInfo {
    private int errCode;

    private String errMessage = null;

    private boolean isPass;

    /**
     * 可以标识错误发生点的坐标的描述信息
     */
    private String flag;

    /**
     * 子验证信息池
     */
    private List<VerifyInfo> subVerifies;

    public VerifyInfo() {
        this.isPass = true;
    }

    public VerifyInfo(String errMessage) {
        this(null, null, errMessage);
    }

    public VerifyInfo(Integer errCode, String errMessage) {
        this(errCode, null, errMessage);
    }

    public VerifyInfo(String flag, String errMessage) {
        this(null, flag, errMessage);
    }

    public VerifyInfo(Integer errCode, String flag, String errMessage) {
        this.errCode = errCode == null ? 0 : errCode;
        this.flag = flag == null ? "" : flag;
        this.errMessage = errMessage == null ? "" : errMessage;
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

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void append(VerifyInfo verifyInfo) {
        if (this.subVerifies == null) {
            subVerifies = new ArrayList<>();
        }
        subVerifies.add(verifyInfo);
    }

    public List<VerifyInfo> getSubVerifies() {
        return this.subVerifies;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        if (!isBlack(this.flag)) {
            stringBuilder.append("flag: ");
            stringBuilder.append(this.flag);
            stringBuilder.append(", ");
        }
        stringBuilder.append("errCode: ");
        stringBuilder.append(this.errCode);
        if (!isBlack(this.errMessage)) {
            stringBuilder.append(", errMessage: ");
            stringBuilder.append(this.errMessage);
        }
        if (this.subVerifies != null && this.subVerifies.size() > 0) {
            stringBuilder.append(", subVerifies: [");
            for (VerifyInfo verifyInfo : subVerifies) {
                stringBuilder.append(verifyInfo.toString());
                stringBuilder.append(", ");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            stringBuilder.append("]");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private boolean isBlack(String value) {
        return value == null || "".equals(value);
    }
}
