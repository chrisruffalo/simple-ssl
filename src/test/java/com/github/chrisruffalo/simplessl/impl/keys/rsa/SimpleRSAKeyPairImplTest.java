package com.github.chrisruffalo.simplessl.impl.keys.rsa;

import com.github.chrisruffalo.simplessl.api.keys.SimpleKeyPair;
import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAKeyPair;
import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAPrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAPublicKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by cruffalo on 2/25/15.
 */
public class SimpleRSAKeyPairImplTest {

    @Test
    public void testGenerateDefault() {
        final SimpleKeyPair pair = SimpleRSAKeyPairImpl.generate();
        Assert.assertNotNull(pair);
        Assert.assertTrue(pair.privateKey() instanceof SimpleRSAPrivateKey);
    }

    @Test
    @Ignore("Takes a long time, only run if large keys are suspect") // takes a long-ish time
    public void testGenerateLarge() {
        final SimpleKeyPair pair = SimpleRSAKeyPairImpl.generate(4096);
        Assert.assertNotNull(pair);
        Assert.assertTrue(pair.privateKey() instanceof SimpleRSAPrivateKey);
    }

    @Test
    public void testGenerateExponent() {
        // generate with a new exponent (bad)
        final SimpleKeyPair pair = SimpleRSAKeyPairImpl.generate(2048, 3);
        Assert.assertNotNull(pair);
        Assert.assertTrue(pair.privateKey() instanceof SimpleRSAPrivateKey);
   }

    @Test
    public void testGenerateSmall() {
        final SimpleKeyPair pair = SimpleRSAKeyPairImpl.generate(512);
        Assert.assertNotNull(pair);
        Assert.assertTrue(pair.privateKey() instanceof SimpleRSAPrivateKey);
    }

    @Test
    public void testPublicFromPrivate() {
        // get pair
        final SimpleRSAKeyPair pair = SimpleRSAKeyPairImpl.generate();

        // get components
        final SimpleRSAPublicKey publicKey = pair.publicKey();
        final SimpleRSAPrivateKey privateKey = pair.privateKey();

        // get public key from private key
        final Attempt<SimpleRSAPublicKey> publicKeyReconstructedOption = privateKey.publicKey();

        // should have option
        Assert.assertTrue(publicKeyReconstructedOption.successful());

        // get from option
        final SimpleRSAPublicKey fromPrivate = publicKeyReconstructedOption.get();

        // is NOT the SAME object
        Assert.assertNotSame(publicKey, fromPrivate);

        // check values
        Assert.assertEquals(publicKey.getModulus(), fromPrivate.getModulus());
        Assert.assertEquals(publicKey.getPublicExponent(), fromPrivate.getPublicExponent());

        // pem encoding is same
        final String pubPem = new String(publicKey.pem());
        final String othPem = new String(fromPrivate.pem());
        Assert.assertEquals(pubPem, othPem);
    }
}

