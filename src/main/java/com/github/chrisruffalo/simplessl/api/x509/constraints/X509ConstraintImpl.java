package com.github.chrisruffalo.simplessl.api.x509.constraints;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/**
 * Created by cruffalo on 5/19/15.
 */
public class X509ConstraintImpl<T> implements X509Constraint<T> {

    private final String name;
    private final String oid;

    protected X509ConstraintImpl(final String name, final String oid) {
        this.name = name;
        // attempt to resolve oid if not provided
        if(oid == null || oid.isEmpty()) {
            final ASN1ObjectIdentifier asn1OID = ASN1ObjectIdentifier.getInstance(oid.getBytes());
            if(asn1OID != null) {
                final String oidString = asn1OID.getId();
                if (oidString != null && !oidString.isEmpty()) {
                    this.oid = oidString;
                } else {
                    this.oid = null;
                }
            } else {
                this.oid = null;
            }
        } else {
            this.oid = oid;
        }
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String oid() {
        return this.oid;
    }

    public ASN1ObjectIdentifier ASN1ID() {
        // nothing to do if we don't have a correct OID
        if(this.oid == null) {
            return null;
        }
        return new ASN1ObjectIdentifier(this.oid);
    }
}
