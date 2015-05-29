package com.github.chrisruffalo.simplessl.impl.certificates;

import com.github.chrisruffalo.simplessl.SimpleSSL;
import com.github.chrisruffalo.simplessl.api.certificates.Certificate;
import com.github.chrisruffalo.simplessl.api.certificates.CertificateBuilder;
import com.github.chrisruffalo.simplessl.api.keys.SimpleKeyPair;
import com.github.chrisruffalo.simplessl.api.keys.SimplePrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.SimplePublicKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by cruffalo on 2/26/15.
 */
public class CertificateBuilderImplTest {

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
        Attempt<SimplePrivateKey<SimplePublicKey>> privateKey = SimpleSSL.RSA().read(privateKeyPath);
        Attempt<SimplePublicKey> publicKey = SimpleSSL.RSA().read(publicKeyPath);

        // create certificate builder
        final CertificateBuilder builder = SimpleSSL.X509().builder();

        builder.setPrivateKey(privateKey.get())
                .setPublicKey(publicKey.get())
        ;

        Attempt<Certificate> cert = builder.build();
    }

    @Test
    public void testDefaultV1FromGenerated() {
        // generate keys
        SimpleKeyPair pair = SimpleSSL.RSA().generate();

        // create certificate builder
        final CertificateBuilder builder = SimpleSSL.X509().builder();

        builder.setPrivateKey(pair.privateKey())
                .setPublicKey(pair.publicKey())
        ;

        Attempt<Certificate> cert = builder.build();
    }

}
