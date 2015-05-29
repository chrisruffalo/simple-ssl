package com.github.chrisruffalo.simplessl.commands;

import com.github.chrisruffalo.simplessl.SimpleSSL;
import com.github.chrisruffalo.simplessl.api.x509.SimpleX509Certificate;
import com.github.chrisruffalo.simplessl.api.x509.SimpleX509CertificateBuilder;
import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAKeyPair;
import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAPrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAPublicKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.util.TempUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;

/**
 * Created by cruffalo on 5/20/15.
 */
public class X509Test {

    @Test
    public void testBasicV1Cert() {

        // generate keypair
        final SimpleRSAKeyPair pair = SimpleSSL.RSA().generate();
        final SimpleRSAPublicKey publicKey = pair.publicKey();
        final SimpleRSAPrivateKey privateKey = pair.privateKey();

        // create v1 cert
        final SimpleX509CertificateBuilder builder = SimpleSSL.X509().builder();
        builder.setPublicKey(publicKey)
               .setPrivateKey(privateKey)
               ;
        // build cert
        final Attempt<SimpleX509Certificate> certAttempt = builder.build();

        // check cert was returned
        Assert.assertFalse(certAttempt.failed());

        // get cert
        final SimpleX509Certificate certificate = certAttempt.get();

        // write and read cert
        final Path tempDir = TempUtil.get();
        final Path certOUt = tempDir.resolve("basic-v1-cert-test.crt");
        SimpleSSL.X509().write(certificate, certOUt);

        // read cert
        final Attempt<SimpleX509Certificate> readAttempt = SimpleSSL.X509().read(certOUt);
        final SimpleX509Certificate readCert = readAttempt.orRuntimeException();
    }

}
