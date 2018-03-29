package com.peramdy.config.execption;

/**
 * @author pd
 */
public class PdException extends RuntimeException {

    public PdException() {
        super();
    }

    public PdException(String message) {
        super(message);
    }

    public PdException(String message, Throwable cause) {
        super(message, cause);
    }

    public PdException(Throwable cause) {
        super(cause);
    }
}
