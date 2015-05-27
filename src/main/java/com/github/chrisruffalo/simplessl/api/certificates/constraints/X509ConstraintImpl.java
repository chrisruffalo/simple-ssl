package com.github.chrisruffalo.simplessl.api.certificates.constraints;

/**
 * Created by cruffalo on 5/19/15.
 */
public class X509ConstraintImpl<T> implements X509Constraint<T> {

    private final String name;
    private final String oid;

    protected X509ConstraintImpl(final String name, final String oid) {
        this.name = name;
        this.oid = oid;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String oid() {
        return this.oid;
    }
}
