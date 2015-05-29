package com.github.chrisruffalo.simplessl.api.keys;

import com.github.chrisruffalo.simplessl.api.model.Attempt;

/**
 * Created by cruffalo on 2/25/15.
 */
public interface SimplePrivateKey<K extends SimplePublicKey> extends SimpleKey, java.security.PrivateKey {

    Attempt<K> publicKey();

    java.security.PrivateKey unwrap();

}
