package com.github.chrisruffalo.simplessl.impl.certificates.readers;

import com.github.chrisruffalo.simplessl.api.certificates.Certificate;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.api.model.Error;
import com.github.chrisruffalo.simplessl.impl.SimpleReader;
import com.github.chrisruffalo.simplessl.impl.certificates.CertificateImpl;
import com.google.common.io.ByteStreams;
import org.bouncycastle.cert.X509CertificateHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cruffalo on 5/26/15.
 */
public class X509DERCertificateReader extends SimpleReader<Certificate> implements X509CertificateReader {

    @Override
    public <T extends Certificate> Attempt<T> read(byte[] bytes) {
        try {
            final X509CertificateHolder holder = new X509CertificateHolder(bytes);
            final Certificate certificate = new CertificateImpl(holder);
            return Attempt.succeed((T)certificate);
        } catch (IOException e) {
            return Attempt.fail(new Error("Failed to parse binary X509 data", e));
        }
    }

    @Override
    public <T extends Certificate> Attempt<T> read(final InputStream inputStream) {

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ByteStreams.copy(inputStream, baos);
        } catch (IOException e) {
            return Attempt.fail(new Error("Could not read binary certificate data", e));
        }

        return this.read(baos.toByteArray());
    }
}
