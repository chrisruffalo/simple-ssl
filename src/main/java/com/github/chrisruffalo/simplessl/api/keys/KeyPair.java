package com.github.chrisruffalo.simplessl.api.keys;

/**
 * Created by cruffalo on 2/24/15.
 */
public interface KeyPair {

    PrivateKey privateKey();

    PublicKey publicKey();

    java.security.KeyPair unwrap();

}
