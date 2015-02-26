package com.github.chrisruffalo.simplessl.impl;

import com.github.chrisruffalo.simplessl.KeyPair;
import com.github.chrisruffalo.simplessl.RSAKeyPair;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by cruffalo on 2/25/15.
 */
public class KeyPairImplTest {

    private static KeyPair pregenPair;

    @BeforeClass
    public static void generate() {
        KeyPairImplTest.pregenPair = RSAKeyPair.generate();
    }

    @Test
    public void testUnwrap() {
        // get pre-generated keypair
        final KeyPair pair = KeyPairImplTest.pregenPair;

        // unwrap for test
        final java.security.KeyPair unwrapped = pair.unwrap();

        // should not be null
        Assert.assertNotNull(unwrapped);
    }


}
