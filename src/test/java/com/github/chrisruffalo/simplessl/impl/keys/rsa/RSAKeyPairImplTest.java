package com.github.chrisruffalo.simplessl.impl.keys.rsa;

import com.github.chrisruffalo.simplessl.api.keys.KeyPair;
import com.github.chrisruffalo.simplessl.api.keys.rsa.RSAKeyPair;
import com.github.chrisruffalo.simplessl.api.keys.rsa.RSAPrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.rsa.RSAPublicKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by cruffalo on 2/25/15.
 */
public class RSAKeyPairImplTest {

    @Test
    public void testGenerateDefault() {
        final KeyPair pair = RSAKeyPairImpl.generate();
        Assert.assertNotNull(pair);
        Assert.assertTrue(pair.privateKey() instanceof RSAPrivateKey);
    }

    @Test
    @Ignore("Takes a long time, only run if large keys are suspect") // takes a long-ish time
    public void testGenerateLarge() {
        final KeyPair pair = RSAKeyPairImpl.generate(4096);
        Assert.assertNotNull(pair);
        Assert.assertTrue(pair.privateKey() instanceof RSAPrivateKey);
    }

    @Test
    public void testGenerateExponent() {
        // generate with a new exponent (bad)
        final KeyPair pair = RSAKeyPairImpl.generate(2048, 3);
        Assert.assertNotNull(pair);
        Assert.assertTrue(pair.privateKey() instanceof RSAPrivateKey);
   }

    @Test
    public void testGenerateSmall() {
        final KeyPair pair = RSAKeyPairImpl.generate(512);
        Assert.assertNotNull(pair);
        Assert.assertTrue(pair.privateKey() instanceof RSAPrivateKey);
    }

    @Test
    public void testPublicFromPrivate() {
        // get pair
        final RSAKeyPair pair = RSAKeyPairImpl.generate();

        // get components
        final RSAPublicKey publicKey = pair.publicKey();
        final RSAPrivateKey privateKey = pair.privateKey();

        // get public key from private key
        final Attempt<RSAPublicKey> publicKeyReconstructedOption = privateKey.publicKey();

        // should have option
        Assert.assertTrue(publicKeyReconstructedOption.successful());

        // get from option
        final RSAPublicKey fromPrivate = publicKeyReconstructedOption.get();

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

