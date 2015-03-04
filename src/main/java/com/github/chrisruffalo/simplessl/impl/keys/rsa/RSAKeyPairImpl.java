package com.github.chrisruffalo.simplessl.impl.keys.rsa;

import com.github.chrisruffalo.simplessl.api.SupportedKeyPairType;
import com.github.chrisruffalo.simplessl.api.keys.rsa.RSAKeyPair;
import com.github.chrisruffalo.simplessl.engine.Provider;
import com.github.chrisruffalo.simplessl.impl.keys.KeyPairImpl;

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
public final class RSAKeyPairImpl extends KeyPairImpl implements RSAKeyPair {

    // decent bitsize by default
    private static final int DEFAULT_BITSIZE = 2048;

    // prevent small rsa exponent attacks with sensible default
    private static final BigInteger DEFAULT_EXPONENT = RSAKeyGenParameterSpec.F4;

    // can't construct
    private RSAKeyPairImpl(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
        super(new RSAPublicKeyImpl(rsaPublicKey), new RSAPrivateKeyImpl(rsaPrivateKey));
    }

    public static RSAKeyPair generate() {
        return RSAKeyPairImpl.generate(RSAKeyPairImpl.DEFAULT_BITSIZE, RSAKeyPairImpl.DEFAULT_EXPONENT);
    }

    public static RSAKeyPair generate(int bits) {
        return RSAKeyPairImpl.generate(bits, RSAKeyPairImpl.DEFAULT_EXPONENT);
    }

    public static RSAKeyPair generate(int bits, long exponent) {
        final BigInteger bigE = BigInteger.valueOf(exponent);
        return RSAKeyPairImpl.generate(bits, bigE);
    }

    public static RSAKeyPair generate(int bits, BigInteger exponent) {

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
            return new RSAKeyPairImpl(pubImpl, priImpl);
        }

        // something has gone wrong!
        return null;
    }
}
