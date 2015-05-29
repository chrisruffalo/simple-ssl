package com.github.chrisruffalo.simplessl.impl.x509;

import com.github.chrisruffalo.simplessl.SimpleSSL;
import com.github.chrisruffalo.simplessl.api.x509.SimpleX509Certificate;
import com.github.chrisruffalo.simplessl.api.x509.SimpleX509CertificateBuilder;
import com.github.chrisruffalo.simplessl.api.keys.SimpleKeyPair;
import com.github.chrisruffalo.simplessl.api.keys.SimplePrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.SimplePublicKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.util.TempUtil;
import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by cruffalo on 2/26/15.
 */
public class SimpleX509CertificateBuilderImplTest {

    @Test
    public void testDefaultV1FromOpenSSLFiles() throws URISyntaxException {
        // url
        final URL privateKeyURL = ClassLoader.getSystemClassLoader().getResource("read-test/rsa/pem/private_key.pem");

        // get path from url (through uri)
        final Path privateKeyPath = Paths.get(privateKeyURL.toURI());

        // url
        final URL publicKeyURL = ClassLoader.getSystemClassLoader().getResource("read-test/rsa/pem/public_key.pem");

        // get path from url (through uri)
        final Path publicKeyPath = Paths.get(publicKeyURL.toURI());

        // read keys
        final Attempt<SimplePrivateKey<SimplePublicKey>> privateKey = SimpleSSL.RSA().read(privateKeyPath);
        final Attempt<SimplePublicKey> publicKey = SimpleSSL.RSA().read(publicKeyPath);

        // create certificate builder
        final SimpleX509CertificateBuilder builder = SimpleSSL.X509().builder();

        builder.setPrivateKey(privateKey.get())
                .setPublicKey(publicKey.get())
        ;

        final Attempt<SimpleX509Certificate> cert = builder.build();
        Assert.assertTrue(cert.successful());
    }

    @Test
    public void testDefaultV1FromGenerated() {
        // generate keys
        final SimpleKeyPair pair = SimpleSSL.RSA().generate();

        // create certificate builder
        final SimpleX509CertificateBuilder builder = SimpleSSL.X509().builder();

        builder.setPrivateKey(pair.privateKey())
                .setPublicKey(pair.publicKey())
        ;

        final Attempt<SimpleX509Certificate> cert = builder.build();
        Assert.assertTrue(cert.successful());
    }

    @Test
    public void testDefaultV3FromGenerated() {
        // generate keys
        final SimpleKeyPair pair = SimpleSSL.RSA().generate();

        // create certificate builder
        final SimpleX509CertificateBuilder builder = SimpleSSL.X509().builder();

        builder.setPrivateKey(pair.privateKey())
                .setPublicKey(pair.publicKey())
                .extend()
                .useAsCA(true)
        ;

        final Attempt<SimpleX509Certificate> attempt = builder.build();
        Assert.assertTrue(attempt.successful());
        final SimpleX509Certificate cert= attempt.get();

        // output
        final Path path = TempUtil.get().resolve("test-default-v3-cert.crt");
        SimpleSSL.X509().write(cert, path);
    }

}
