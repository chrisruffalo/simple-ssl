package com.github.chrisruffalo.simplessl.impl.keys;

import com.github.chrisruffalo.simplessl.api.keys.SimpleKeyPair;
import com.github.chrisruffalo.simplessl.api.keys.SimplePrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.SimplePublicKey;

/**
 * Created by cruffalo on 2/25/15.
 */
public class SimpleKeyPairImpl implements SimpleKeyPair {

    private final SimplePublicKey publicKey;

    private final SimplePrivateKey privateKey;

    public SimpleKeyPairImpl(SimplePublicKey publicKey, SimplePrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public SimplePrivateKey privateKey() {
        return this.privateKey;
    }

    @Override
    public SimplePublicKey publicKey() {
        return this.publicKey;
    }

    @Override
    public java.security.KeyPair unwrap() {
        return new java.security.KeyPair(this.publicKey.unwrap(), this.privateKey.unwrap());
    }
}
