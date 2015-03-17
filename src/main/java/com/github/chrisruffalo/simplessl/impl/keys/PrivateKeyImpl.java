package com.github.chrisruffalo.simplessl.impl.keys;

import com.github.chrisruffalo.simplessl.api.keys.PrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.PublicKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;

/**
 * Created by cruffalo on 2/25/15.
 */
public class PrivateKeyImpl<K extends PublicKey> extends KeyImpl implements PrivateKey<K> {

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

    @Override
    public Attempt<K> publicKey() {
        // not enough information
        return Attempt.fail("By default there is not enough information to reconstruct a public key from existing information");
    }
}
