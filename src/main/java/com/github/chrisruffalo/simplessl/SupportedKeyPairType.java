package com.github.chrisruffalo.simplessl;

/**
 * Created by cruffalo on 2/24/15.
 */
public enum SupportedKeyPairType {

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
