package com.github.chrisruffalo.simplessl.impl.certificates;

import com.github.chrisruffalo.simplessl.api.certificates.Certificate;
import com.github.chrisruffalo.simplessl.Certificates;
import com.github.chrisruffalo.simplessl.Keys;
import com.github.chrisruffalo.simplessl.api.certificates.CertificateBuilder;
import com.github.chrisruffalo.simplessl.api.keys.KeyPair;
import com.github.chrisruffalo.simplessl.api.keys.PrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.PublicKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.impl.keys.rsa.RSAKeyPairImpl;
import com.google.common.base.Optional;
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
        Attempt<PrivateKey<PublicKey>> privateKey = Keys.read(privateKeyPath);
        Attempt<PublicKey> publicKey = Keys.read(publicKeyPath);

        // create certificate builder
        final CertificateBuilder builder = Certificates.builder();

        builder.setPrivateKey(privateKey.get())
                .setPublicKey(publicKey.get())
        ;

        Optional<Certificate> cert = builder.build();
    }

    @Test
    public void testDefaultV1FromGenerated() {
        // generate keys
        KeyPair pair = Keys.generateRSA();

        // create certificate builder
        final CertificateBuilder builder = Certificates.builder();

        builder.setPrivateKey(pair.privateKey())
                .setPublicKey(pair.publicKey())
        ;

        Optional<Certificate> cert = builder.build();
    }

}
