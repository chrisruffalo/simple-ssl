package com.github.chrisruffalo.simplessl.impl;

import com.github.chrisruffalo.simplessl.PublicKey;

/**
 * Created by cruffalo on 2/25/15.
 */
public class PublicKeyImpl extends KeyImpl implements PublicKey {

    private final java.security.PublicKey wrapped;

    public PublicKeyImpl(java.security.PublicKey key) {
        this.wrapped = key;
    }

    @Override
    public boolean isPrivate() {
        return false;
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public java.security.PublicKey unwrap() {
        return this.wrapped;
    }

}
