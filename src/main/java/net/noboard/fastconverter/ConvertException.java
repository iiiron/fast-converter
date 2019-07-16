package net.noboard.fastconverter;

/**
 * @author wanxm
 */
public class ConvertException extends RuntimeException {
    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(Throwable e) {
        super(e);
    }

    public ConvertException(String message, Throwable e) {
        super(message,e);
    }
}
