package com.github.chrisruffalo.simplessl.api.keys;

/**
 * Created by cruffalo on 2/25/15.
 */
public interface PrivateKey extends Key, java.security.PrivateKey {

    java.security.PrivateKey unwrap();

}
