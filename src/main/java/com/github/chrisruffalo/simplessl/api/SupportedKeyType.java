package com.github.chrisruffalo.simplessl.api;

/**
 * Created by cruffalo on 2/24/15.
 */
public enum SupportedKeyType {

    RSA("RSA"),
    DSA("DSA")
    ;

    private final String type;

    private SupportedKeyType(String typeString) {
        this.type = typeString;
    }

    @Override
    public String toString() {
        return type;
    }

}

