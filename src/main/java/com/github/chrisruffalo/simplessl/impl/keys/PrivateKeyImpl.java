package com.github.chrisruffalo.simplessl.impl.keys;

import com.github.chrisruffalo.simplessl.api.keys.PrivateKey;

/**
 * Created by cruffalo on 2/25/15.
 */
public class PrivateKeyImpl extends KeyImpl implements PrivateKey {

    private final java.security.PrivateKey wrapped;

    public PrivateKeyImpl(java.security.PrivateKey key) {
        this.wrapped = key;
    }

    @Override
    public java.security.PrivateKey unwrap() {
        return this.wrapped;
    }

    @Override
    public boolean isPrivate() {
        return true;
    }

    @Override
    public boolean isPublic() { return false; }

}
