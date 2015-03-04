package com.github.chrisruffalo.simplessl.impl.keys.rsa;

import com.github.chrisruffalo.simplessl.api.keys.rsa.RSAPublicKey;
import com.github.chrisruffalo.simplessl.impl.keys.PublicKeyImpl;

import java.math.BigInteger;
import java.security.Key;
import java.security.PublicKey;

/**
 * Created by cruffalo on 3/3/15.
 */
public class RSAPublicKeyImpl extends PublicKeyImpl implements RSAPublicKey {

    public RSAPublicKeyImpl(java.security.interfaces.RSAPublicKey key) {
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
