package com.github.chrisruffalo.simplessl;

import com.github.chrisruffalo.simplessl.engine.Provider;
import com.github.chrisruffalo.simplessl.impl.KeyPairImpl;
import com.github.chrisruffalo.simplessl.impl.PrivateKeyImpl;
import com.github.chrisruffalo.simplessl.impl.PublicKeyImpl;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;


/**
 * Created by cruffalo on 2/24/15.
 */
public final class RSAKeyPair {

    // decent bitsize by default
    private static final int DEFAULT_BITSIZE = 2048;

    // prevent small rsa exponent attacks with sensible default
    private static final BigInteger DEFAULT_EXPONENT = RSAKeyGenParameterSpec.F4;

    // empty util so cannot construct
    private RSAKeyPair() {

    }

    public static KeyPair generate() {
        return RSAKeyPair.generate(RSAKeyPair.DEFAULT_BITSIZE, RSAKeyPair.DEFAULT_EXPONENT);
    }

    public static KeyPair generate(int bits) {
        return RSAKeyPair.generate(bits, RSAKeyPair.DEFAULT_EXPONENT);
    }

    public static KeyPair generate(int bits, long exponent) {
        final BigInteger bigE = BigInteger.valueOf(exponent);
        return RSAKeyPair.generate(bits, bigE);
    }

    public static KeyPair generate(int bits, BigInteger exponent) {

        // get generator
        final KeyPairGenerator generator = Provider.getKeyPairGenerator(SupportedKeyPairType.RSA, bits, exponent);

        // generate wrapped key
        final java.security.KeyPair pair = generator.generateKeyPair();

        // return newly created impl
        return new KeyPairImpl(new PublicKeyImpl(pair.getPublic()), new PrivateKeyImpl(pair.getPrivate()));
    }
}
