package net.noboard.fastconverter;

/**
 * @author wanxm
 */
public class ConvertException extends Exception {
    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(Exception e) {
        super(e);
    }
}
