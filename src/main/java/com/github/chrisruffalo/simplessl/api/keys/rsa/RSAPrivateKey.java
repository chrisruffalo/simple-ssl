package com.github.chrisruffalo.simplessl.api.keys.rsa;

import com.github.chrisruffalo.simplessl.api.keys.Key;
import com.github.chrisruffalo.simplessl.api.keys.PrivateKey;
import com.google.common.base.Optional;

/**
 * Created by cruffalo on 3/3/15.
 */
public interface RSAPrivateKey extends RSAKey, PrivateKey<RSAPublicKey>, Key, java.security.interfaces.RSAPrivateCrtKey {

    Optional<RSAPublicKey> publicKey();

}
