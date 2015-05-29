package com.github.chrisruffalo.simplessl.api.keys.rsa;

import com.github.chrisruffalo.simplessl.api.keys.SimpleKeyPair;

/**
 * Created by cruffalo on 3/3/15.
 */
public interface SimpleRSAKeyPair extends SimpleKeyPair {

    SimpleRSAPrivateKey privateKey();

    SimpleRSAPublicKey publicKey();
}
