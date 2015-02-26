package com.github.chrisruffalo.simplessl;

/**
 * Created by cruffalo on 2/25/15.
 */
public interface PublicKey extends Key, java.security.PublicKey{

    java.security.PublicKey unwrap();
}
