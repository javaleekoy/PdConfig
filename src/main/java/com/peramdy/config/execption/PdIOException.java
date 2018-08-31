package com.peramdy.config.execption;

/**
 * @author peramdy on 2018/8/15.
 */
public class PdIOException extends PdException {

    public PdIOException() {
        super();
    }

    public PdIOException(String message) {
        super(message);
    }

    public PdIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public PdIOException(Throwable cause) {
        super(cause);
    }
}
