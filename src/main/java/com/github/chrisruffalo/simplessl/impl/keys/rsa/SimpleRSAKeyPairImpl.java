package com.github.chrisruffalo.simplessl.impl.keys.rsa;

import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAKeyPair;
import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAPrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAPublicKey;
import com.github.chrisruffalo.simplessl.engine.Provider;
import com.github.chrisruffalo.simplessl.impl.keys.SimpleKeyPairImpl;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;


/**
 * Created by cruffalo on 2/24/15.
 */
public final class SimpleRSAKeyPairImpl extends SimpleKeyPairImpl implements SimpleRSAKeyPair {

    // decent bitsize by default
    private static final int DEFAULT_BITSIZE = 2048;

    // prevent small rsa exponent attacks with sensible default
    private static final BigInteger DEFAULT_EXPONENT = RSAKeyGenParameterSpec.F4;

    // can't construct
    private SimpleRSAKeyPairImpl(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
        super(new SimpleRSAPublicKeyImpl(rsaPublicKey), new SimpleRSAPrivateKeyImpl(rsaPrivateKey));
    }

    @Override
    public SimpleRSAPrivateKey privateKey() {
        return (SimpleRSAPrivateKey) super.privateKey();
    }

    @Override
    public SimpleRSAPublicKey publicKey() {
        return (SimpleRSAPublicKey) super.publicKey();
    }

    // generators ======================

    public static SimpleRSAKeyPair generate() {
        return SimpleRSAKeyPairImpl.generate(SimpleRSAKeyPairImpl.DEFAULT_BITSIZE, SimpleRSAKeyPairImpl.DEFAULT_EXPONENT);
    }

    public static SimpleRSAKeyPair generate(int bits) {
        return SimpleRSAKeyPairImpl.generate(bits, SimpleRSAKeyPairImpl.DEFAULT_EXPONENT);
    }

    public static SimpleRSAKeyPair generate(int bits, long exponent) {
        final BigInteger bigE = BigInteger.valueOf(exponent);
        return SimpleRSAKeyPairImpl.generate(bits, bigE);
    }

    public static SimpleRSAKeyPair generate(int bits, BigInteger exponent) {

        // get generator
        final KeyPairGenerator generator = Provider.getRSAKeyPairGenerator(bits, exponent);

        // generate wrapped key
        final java.security.KeyPair pair = generator.generateKeyPair();

        // get keys
        final PublicKey publicKey = pair.getPublic();
        final PrivateKey privateKey = pair.getPrivate();

        // check
        if(publicKey instanceof RSAPublicKey && privateKey instanceof RSAPrivateKey) {
            // cast
            final RSAPublicKey pubImpl = (RSAPublicKey)publicKey;
            final RSAPrivateKey priImpl = (RSAPrivateKey)privateKey;

            // return newly created impl
            return new SimpleRSAKeyPairImpl(pubImpl, priImpl);
        }

        // something has gone wrong!
        return null;
    }
}
