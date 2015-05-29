package com.github.chrisruffalo.simplessl.impl.x509.readers;

import com.github.chrisruffalo.simplessl.api.x509.SimpleX509Certificate;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.impl.SimpleReader;
import com.github.chrisruffalo.simplessl.impl.x509.SimpleX509CertificateImpl;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.PEMParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by cruffalo on 5/26/15.
 */
public class X509PEMCertificateReader extends SimpleReader<SimpleX509Certificate> implements X509CertificateReader {

    @Override
    public <T extends SimpleX509Certificate> Attempt<T> read(final InputStream inputStream) {

        // create source reader from input stream
        try(final Reader sourceReader = new InputStreamReader(inputStream)) {
            // create pem reader
            final PEMParser parser = new PEMParser(sourceReader);
            try {
                // get decoded/parsed object from reader
                final Object decoded = parser.readObject();
                if(decoded instanceof X509CertificateHolder) {
                    final X509CertificateHolder holder = (X509CertificateHolder)decoded;
                    return Attempt.succeed((T)new SimpleX509CertificateImpl(holder));
                }
            } catch (IOException e) {
                return Attempt.fail("Failed to open object as PEM: " + e.getMessage());
            }
        } catch (IOException e) {
            return Attempt.fail("Could not open x509 certificate file: " + e.getMessage(), e);
        }

        return Attempt.fail("Could not decode x509 certificate from PEM data");
    }
}
