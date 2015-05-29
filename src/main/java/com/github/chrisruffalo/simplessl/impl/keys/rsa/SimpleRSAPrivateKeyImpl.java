package com.github.chrisruffalo.simplessl.impl.keys.rsa;

import com.github.chrisruffalo.simplessl.api.SupportedKeyType;
import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAPrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAPublicKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.engine.Provider;
import com.github.chrisruffalo.simplessl.impl.keys.SimplePrivateKeyImpl;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by cruffalo on 3/3/15.
 */
public class SimpleRSAPrivateKeyImpl extends SimplePrivateKeyImpl<SimpleRSAPublicKey> implements SimpleRSAPrivateKey {

    public SimpleRSAPrivateKeyImpl(java.security.interfaces.RSAPrivateKey key) {
        super(key);
    }

    @Override
    public Attempt<SimpleRSAPublicKey> publicKey() {
        final Key key = this.unwrap();
        if(key instanceof RSAPrivateCrtKey) {
            // get spec
            final RSAPrivateCrtKey crtKey = (RSAPrivateCrtKey) key;
            final RSAPublicKeySpec spec = new RSAPublicKeySpec(crtKey.getModulus(), crtKey.getPublicExponent());

            // derive public key from spec
            final KeyFactory factory = Provider.getKeyFactory(SupportedKeyType.RSA);
            try {
                // create key from factory
                final java.security.PublicKey publicKey = factory.generatePublic(spec);

                // if it is found, return it
                if(publicKey instanceof java.security.interfaces.RSAPublicKey) {
                    final java.security.interfaces.RSAPublicKey rsaPublicKey = (java.security.interfaces.RSAPublicKey)publicKey;
                    final SimpleRSAPublicKey impl = new SimpleRSAPublicKeyImpl(rsaPublicKey);
                    return Attempt.succeed(impl);
                }
            } catch (InvalidKeySpecException e) {
                return Attempt.fail("An invalid public key specification was provided for reconstruction of the public key", e);
            }
        }
        return Attempt.fail("Insufficient information for reconstructing the public key.");
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

    @Override
    public BigInteger getPublicExponent() {
        final PrivateKey key = this.unwrap();
        if(key instanceof java.security.interfaces.RSAPrivateCrtKey) {
            return ((RSAPrivateCrtKey) key).getPublicExponent();
        }
        return null;
    }

    @Override
    public BigInteger getPrimeP() {
        final PrivateKey key = this.unwrap();
        if(key instanceof java.security.interfaces.RSAPrivateCrtKey) {
            return ((RSAPrivateCrtKey) key).getPrimeP();
        }
        return null;
    }

    @Override
    public BigInteger getPrimeQ() {
        final PrivateKey key = this.unwrap();
        if(key instanceof java.security.interfaces.RSAPrivateCrtKey) {
            return ((RSAPrivateCrtKey) key).getPrimeQ();
        }
        return null;
    }

    @Override
    public BigInteger getPrimeExponentP() {
        final PrivateKey key = this.unwrap();
        if(key instanceof java.security.interfaces.RSAPrivateCrtKey) {
            return ((RSAPrivateCrtKey) key).getPrimeExponentP();
        }
        return null;
    }

    @Override
    public BigInteger getPrimeExponentQ() {
        final PrivateKey key = this.unwrap();
        if(key instanceof java.security.interfaces.RSAPrivateCrtKey) {
            return ((RSAPrivateCrtKey) key).getPrimeExponentQ();
        }
        return null;
    }

    @Override
    public BigInteger getCrtCoefficient() {
        final PrivateKey key = this.unwrap();
        if(key instanceof java.security.interfaces.RSAPrivateCrtKey) {
            return ((RSAPrivateCrtKey) key).getCrtCoefficient();
        }
        return null;
    }
}
