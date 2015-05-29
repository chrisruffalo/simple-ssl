package com.github.chrisruffalo.simplessl.api.x509.constraints;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

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

    /**
     * An ASN1 instance ID for compatibility with BCE
     *
     * @return
     */
    ASN1ObjectIdentifier ASN1ID();

}
