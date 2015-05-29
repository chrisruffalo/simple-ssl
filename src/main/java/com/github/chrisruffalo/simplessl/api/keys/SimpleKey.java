package com.github.chrisruffalo.simplessl.api.keys;

/**
 * Created by cruffalo on 2/25/15.
 */
public interface SimpleKey extends java.security.Key {

    boolean isPrivate();

    boolean isPublic();

    byte[] der();

    byte[] pem();

    java.security.Key unwrap();
}
