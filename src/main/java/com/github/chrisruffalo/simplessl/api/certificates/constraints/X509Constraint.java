package com.github.chrisruffalo.simplessl.api.certificates.constraints;

/**
 * A constraint parent (standard, key usage, critical...)
 * taken from: https://www.openssl.org/docs/apps/x509v3_config.html
 */
public interface X509Constraint<T> {

    /**
     * The short name of the constraint
     *
     * @return
     */
    String name();

    /**
     * The dotted oid of the constraint
     *
     * @return
     */
    String oid();

}
