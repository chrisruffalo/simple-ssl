package com.github.chrisruffalo.simplessl.impl.keys.rsa;

import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAPublicKey;
import com.github.chrisruffalo.simplessl.impl.keys.SimplePublicKeyImpl;

import java.math.BigInteger;
import java.security.Key;
import java.security.PublicKey;

/**
 * Created by cruffalo on 3/3/15.
 */
public class SimpleRSAPublicKeyImpl extends SimplePublicKeyImpl implements SimpleRSAPublicKey {

    public SimpleRSAPublicKeyImpl(java.security.interfaces.RSAPublicKey key) {
        super(key);
    }

    @Override
    public BigInteger getModulus() {
        final Key key = this.unwrap();
        if (key instanceof java.security.interfaces.RSAKey) {
            return ((java.security.interfaces.RSAKey) key).getModulus();
        }
        return null;
    }

    @Override
    public BigInteger getPublicExponent() {
        final PublicKey key = this.unwrap();
        if(key instanceof java.security.interfaces.RSAPublicKey) {
            return ((java.security.interfaces.RSAPublicKey) key).getPublicExponent();
        }
        return null;
    }
}
