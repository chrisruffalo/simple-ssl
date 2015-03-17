package com.github.chrisruffalo.simplessl.api.model;

import com.google.common.base.Optional;

import javax.swing.text.html.Option;

/**
 * Created by cruffalo on 3/17/15.
 */
public class Error extends Feedback {

    private final Optional<Throwable> cause;

    public Error(String message) {
        this(message, null);
    }

    public Error(String message, Throwable cause) {
        super(message);
        if(cause == null) {
            this.cause = Optional.absent();
        } else {
            this.cause = Optional.of(cause);
        }
    }

    public boolean hasCause() {
        return this.cause.isPresent();
    }

    public Throwable cause() {
        return this.cause.get();
    }

}
