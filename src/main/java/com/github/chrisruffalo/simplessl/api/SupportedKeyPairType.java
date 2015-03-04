package com.github.chrisruffalo.simplessl.api;

/**
 * Created by cruffalo on 2/24/15.
 */
public enum SupportedKeyPairType {

    DSA("DSA"),
    RSA("RSA")
    ;

    private final String type;

    private SupportedKeyPairType(String typeString) {
        this.type = typeString;
    }

    @Override
    public String toString() {
        return type;
    }

}
