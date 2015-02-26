package com.github.chrisruffalo.simplessl;

import java.nio.file.Path;

/**
 * Created by cruffalo on 2/25/15.
 */
public interface Key extends java.security.Key {

    boolean isPrivate();

    boolean isPublic();

    byte[] der();

    byte[] pem();

    java.security.Key unwrap();
}
