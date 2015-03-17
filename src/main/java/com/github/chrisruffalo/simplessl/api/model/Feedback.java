package com.github.chrisruffalo.simplessl.api.model;

/**
 * Created by cruffalo on 3/17/15.
 */
public abstract class Feedback {

    private final String message;

    public Feedback(String message) {
        if(message == null || message.trim().isEmpty()) {
            throw new IllegalStateException("Cannot create an error from an empty or null message");
        }
        this.message = message;
    }

    public String message() {
        return this.message;
    }

}
