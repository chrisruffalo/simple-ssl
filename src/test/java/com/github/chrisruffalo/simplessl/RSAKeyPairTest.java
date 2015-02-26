package com.github.chrisruffalo.simplessl;

import org.junit.Test;

/**
 * Created by cruffalo on 2/25/15.
 */
public class RSAKeyPairTest {

    @Test
    public void testGenerateDefault() {
        KeyPair pair = RSAKeyPair.generate();
    }

    @Test
    public void testGenerateLarge() {
        KeyPair pair = RSAKeyPair.generate(4096);
    }

    @Test
    public void testGenerateExponent() {
        // generate with a new exponent (bad)
        KeyPair pair = RSAKeyPair.generate(2048, 3);
    }

    @Test
    public void testGenerateSmall() {
        KeyPair pair = RSAKeyPair.generate(512);
    }
}

