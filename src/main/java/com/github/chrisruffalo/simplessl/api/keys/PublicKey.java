package com.github.chrisruffalo.simplessl.api.keys;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

/**
 * Created by cruffalo on 2/25/15.
 */
public interface PublicKey extends Key, java.security.PublicKey {

    SubjectPublicKeyInfo info();

    java.security.PublicKey unwrap();

}
