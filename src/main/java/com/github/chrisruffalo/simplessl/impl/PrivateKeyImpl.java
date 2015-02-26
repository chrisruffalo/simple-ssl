package com.github.chrisruffalo.simplessl.impl;

import com.github.chrisruffalo.simplessl.KeyPair;
import com.github.chrisruffalo.simplessl.PrivateKey;
import com.github.chrisruffalo.simplessl.PublicKey;

/**
 * Created by cruffalo on 2/25/15.
 */
public class PrivateKeyImpl extends KeyImpl implements PrivateKey {

    private final java.security.PrivateKey wrapped;

    public PrivateKeyImpl(java.security.PrivateKey key) {
        this.wrapped = key;
    }

    @Override
    public boolean isPrivate() {
        return true;
    }

    @Override
    public boolean isPublic() { return false; }

    @Override
    public java.security.PrivateKey unwrap() {
        return this.wrapped;
    }

    @Override
    public KeyPair pair() {
        return null;
    }

    @Override
    public PublicKey publicComponent() {
        return null;
    }
}
