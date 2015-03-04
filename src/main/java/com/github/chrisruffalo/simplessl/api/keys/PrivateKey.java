package com.github.chrisruffalo.simplessl.api.keys;

import com.google.common.base.Optional;

/**
 * Created by cruffalo on 2/25/15.
 */
public interface PrivateKey<K extends PublicKey> extends Key, java.security.PrivateKey {

    Optional<K> publicKey();

    java.security.PrivateKey unwrap();

}
