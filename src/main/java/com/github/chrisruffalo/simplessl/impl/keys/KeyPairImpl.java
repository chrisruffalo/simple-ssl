package com.github.chrisruffalo.simplessl.impl.keys;

import com.github.chrisruffalo.simplessl.api.keys.KeyPair;
import com.github.chrisruffalo.simplessl.api.keys.PrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.PublicKey;

/**
 * Created by cruffalo on 2/25/15.
 */
public class KeyPairImpl implements KeyPair {

    private final PublicKey publicKey;

    private final PrivateKey privateKey;

    public KeyPairImpl(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public PrivateKey privateKey() {
        return this.privateKey;
    }

    @Override
    public PublicKey publicKey() {
        return this.publicKey;
    }

    @Override
    public java.security.KeyPair unwrap() {
        return new java.security.KeyPair(this.publicKey.unwrap(), this.privateKey.unwrap());
    }
}
