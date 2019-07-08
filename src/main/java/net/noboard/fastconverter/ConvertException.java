package net.noboard.fastconverter;

/**
 * @author wanxm
 */
public class ConvertException extends RuntimeException {
    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(Exception e) {
        super(e);
    }
}
