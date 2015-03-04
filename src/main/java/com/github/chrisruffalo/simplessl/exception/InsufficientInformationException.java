package com.github.chrisruffalo.simplessl.exception;

/**
 * Created by cruffalo on 3/4/15.
 */
public class InsufficientInformationException extends RuntimeException {

    public InsufficientInformationException() {
        super();
    }

    public InsufficientInformationException(String message) {
        super(message);
    }

    public InsufficientInformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientInformationException(Throwable cause) {
        super(cause);
    }

}
