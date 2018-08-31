package com.peramdy.config.execption;

/**
 * @author peramdy on 2018/8/14.
 */
public class PdNoSuchElementException extends PdException {

    public PdNoSuchElementException() {
        super();
    }

    public PdNoSuchElementException(String message) {
        super(message);
    }

    public PdNoSuchElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public PdNoSuchElementException(Throwable cause) {
        super(cause);
    }
}
