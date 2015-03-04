package com.github.chrisruffalo.simplessl.api.keys.rsa;

import com.github.chrisruffalo.simplessl.api.keys.KeyPair;

/**
 * Created by cruffalo on 3/3/15.
 */
public interface RSAKeyPair extends KeyPair {

    RSAPrivateKey privateKey();

    RSAPublicKey publicKey();
}
