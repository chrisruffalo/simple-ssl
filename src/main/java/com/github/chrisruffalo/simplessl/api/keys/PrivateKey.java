package com.github.chrisruffalo.simplessl.api.keys;

import com.github.chrisruffalo.simplessl.api.model.Attempt;

/**
 * Created by cruffalo on 2/25/15.
 */
public interface PrivateKey<K extends PublicKey> extends Key, java.security.PrivateKey {

    Attempt<K> publicKey();

    java.security.PrivateKey unwrap();

}
