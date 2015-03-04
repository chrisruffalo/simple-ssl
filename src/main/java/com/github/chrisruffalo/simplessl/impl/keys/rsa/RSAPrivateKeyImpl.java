package com.github.chrisruffalo.simplessl.impl.keys.rsa;

import com.github.chrisruffalo.simplessl.api.keys.rsa.RSAPrivateKey;
import com.github.chrisruffalo.simplessl.impl.keys.PrivateKeyImpl;

import java.math.BigInteger;
import java.security.Key;
import java.security.PrivateKey;

/**
 * Created by cruffalo on 3/3/15.
 */
public class RSAPrivateKeyImpl extends PrivateKeyImpl implements RSAPrivateKey {

    public RSAPrivateKeyImpl(java.security.interfaces.RSAPrivateKey key) {
        super(key);
    }

    @Override
    public BigInteger getModulus() {
        final Key key = this.unwrap();
        if(key instanceof java.security.interfaces.RSAKey) {
            return ((java.security.interfaces.RSAKey) key).getModulus();
        }
        return null;
    }

    @Override
    public BigInteger getPrivateExponent() {
        final PrivateKey key = this.unwrap();
        if(key instanceof java.security.interfaces.RSAPrivateKey) {
            return ((java.security.interfaces.RSAPrivateKey) key).getPrivateExponent();
        }
        return null;
    }

}
