package com.github.chrisruffalo.simplessl.impl.keys;

import com.github.chrisruffalo.simplessl.api.keys.SimpleKeyPair;
import com.github.chrisruffalo.simplessl.impl.keys.rsa.SimpleRSAKeyPairImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by cruffalo on 2/25/15.
 */
public class SimpleKeyPairImplTest {

    private static SimpleKeyPair pregenPair;

    @BeforeClass
    public static void generate() {
        com.github.chrisruffalo.simplessl.impl.keys.SimpleKeyPairImplTest.pregenPair = SimpleRSAKeyPairImpl.generate();
    }

    @Test
    public void testUnwrap() {
        // get pre-generated keypair
        final SimpleKeyPair pair = com.github.chrisruffalo.simplessl.impl.keys.SimpleKeyPairImplTest.pregenPair;

        // unwrap for test
        final java.security.KeyPair unwrapped = pair.unwrap();

        // should not be null
        Assert.assertNotNull(unwrapped);
    }


}
