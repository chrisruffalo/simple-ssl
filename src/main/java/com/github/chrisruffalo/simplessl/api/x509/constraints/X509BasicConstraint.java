package com.github.chrisruffalo.simplessl.api.x509.constraints;

/**
 * Created by cruffalo on 5/19/15.
 */
public class X509BasicConstraint<T> extends X509ConstraintImpl<T> {

    public static final X509BasicConstraint<Boolean> CA = new X509BasicConstraint<>("CA", "");
    public static final X509BasicConstraint<Integer> PATH_LENGTH = new X509BasicConstraint<>("pathlen", "");

    private X509BasicConstraint(String name, String oid) {
        super(name, oid);
    }
}
