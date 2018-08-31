package com.peramdy.config.execption;

/**
 * @author peramdy on 2018/8/16.
 */
public class PdIllegaArgumentException extends PdException {

    public PdIllegaArgumentException() {
        super();
    }

    public PdIllegaArgumentException(String message) {
        super(message);
    }

    public PdIllegaArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PdIllegaArgumentException(Throwable cause) {
        super(cause);
    }
}
