package com.github.chrisruffalo.simplessl.api.certificates.constraints;

/**
 * Created by cruffalo on 5/19/15.
 */
public class CertificateBasicConstraint<T> extends X509ConstraintImpl<T> {

    public static final CertificateBasicConstraint<Boolean> CA = new CertificateBasicConstraint<>("CA", "");
    public static final CertificateBasicConstraint<Integer> PATH_LENGTH = new CertificateBasicConstraint<>("pathlen", "");

    private CertificateBasicConstraint(String name, String oid) {
        super(name, oid);
    }
}
