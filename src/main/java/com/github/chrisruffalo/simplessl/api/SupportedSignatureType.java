package com.github.chrisruffalo.simplessl.api;

/**
 * Created by cruffalo on 2/26/15.
 */
public enum SupportedSignatureType {

    // ecdsa
    ECDSA_SHA1("SHA1withECDSA"),
    ECDSA_SHA256("SHA256withECDSA"),
    ECDSA_SHA384("SHA384withECDSA"),
    ECDSA_SHA512("SHA512withECDSA"),

    // dsa
    DSA_NONE("NONEwithDSA"),

    // rsa md
    RSA_MD2("MD2withRSA"),
    RSA_MD5("MD5withRSA"),

    // rsa sha
    RSA_SHA1("SHA1withRSA"),
    RSA_SHA256("SHA256withRSA"),
    RSA_SHA384("SHA384withRSA"),
    RSA_SHA512("SHA512withRSA")
    ;

    private final String type;

    private SupportedSignatureType(String typeString) {
        this.type = typeString;
    }

    @Override
    public String toString() {
        return type;
    }

}
