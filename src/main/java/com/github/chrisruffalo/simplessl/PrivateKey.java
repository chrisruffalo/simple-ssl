package com.github.chrisruffalo.simplessl;

/**
 * Created by cruffalo on 2/25/15.
 */
public interface PrivateKey extends Key, java.security.PrivateKey {

    KeyPair pair();

    PublicKey publicComponent();

    java.security.PrivateKey unwrap();

}
