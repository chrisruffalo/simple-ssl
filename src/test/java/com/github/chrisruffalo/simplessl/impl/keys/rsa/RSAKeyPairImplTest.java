package com.github.chrisruffalo.simplessl.impl.keys.rsa;

import com.github.chrisruffalo.simplessl.api.keys.KeyPair;
import com.github.chrisruffalo.simplessl.impl.keys.rsa.RSAKeyPairImpl;
import org.junit.Test;

/**
 * Created by cruffalo on 2/25/15.
 */
public class RSAKeyPairImplTest {

    @Test
    public void testGenerateDefault() {
        KeyPair pair = RSAKeyPairImpl.generate();
    }

    @Test
    public void testGenerateLarge() {
        KeyPair pair = RSAKeyPairImpl.generate(4096);
    }

    @Test
    public void testGenerateExponent() {
        // generate with a new exponent (bad)
        KeyPair pair = RSAKeyPairImpl.generate(2048, 3);
    }

    @Test
    public void testGenerateSmall() {
        KeyPair pair = RSAKeyPairImpl.generate(512);
    }
}

