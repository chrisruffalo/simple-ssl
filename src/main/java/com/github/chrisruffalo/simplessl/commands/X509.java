package com.github.chrisruffalo.simplessl.commands;

import com.github.chrisruffalo.simplessl.api.certificates.Certificate;
import com.github.chrisruffalo.simplessl.api.certificates.CertificateBuilder;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.api.model.Error;
import com.github.chrisruffalo.simplessl.impl.certificates.CertificateBuilderImpl;
import com.github.chrisruffalo.simplessl.impl.certificates.CertificateImpl;
import com.github.chrisruffalo.simplessl.impl.certificates.readers.X509CertificateReader;
import com.github.chrisruffalo.simplessl.impl.certificates.readers.X509DERCertificateReader;
import com.github.chrisruffalo.simplessl.impl.certificates.readers.X509PEMCertificateReader;
import com.google.common.io.ByteStreams;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cruffalo on 2/26/15.
 */
public final class X509 {

    private final List<X509CertificateReader> readerChain = new ArrayList<>(2);

    public X509() {
        // try pem first
        this.readerChain.add(new X509PEMCertificateReader());
        // then try der
        this.readerChain.add(new X509DERCertificateReader());
    }

    public CertificateBuilder builder() {
        return new CertificateBuilderImpl();
    }

    public Attempt<Certificate> read(final Path path) {
        if(path == null) {
            return Attempt.fail("A non-null path to a valid x509 certificate must be provided");
        }

        if(!Files.exists(path) || !Files.isRegularFile(path)) {
            return Attempt.fail("The file at path '" + path.toString() + "' does not exist or is not a file");
        }

        if(!Files.isReadable(path)) {
            return Attempt.fail("The file at path '" + path.toString() + "' is not readable");
        }

        // todo: gather errors
        for(final X509CertificateReader reader : this.readerChain) {
            final Attempt<Certificate> attempt = reader.read(path);
            if(attempt.successful()) {
                return attempt;
            }
        }

        return Attempt.fail("Could not read certificate data from path: " + path.toString());
    }

    public Attempt<Certificate> read(final InputStream stream) {
        if(stream == null) {
            return Attempt.fail("No certificate data can be read from a null stream");
        }

        // todo: gather errors
        for(final X509CertificateReader reader : this.readerChain) {
            final Attempt<Certificate> attempt = reader.read(stream);
            if(attempt.successful()) {
                return attempt;
            }
        }

        return Attempt.fail("Could not read certificate data from input stream");
    }

    public Attempt<Certificate> read(final byte[] bytes) {
        if(bytes == null || bytes.length < 1) {
            return Attempt.fail("Certificate binary data was empty");
        }

        // todo: gather errors
        for(final X509CertificateReader reader : this.readerChain) {
            final Attempt<Certificate> attempt = reader.read(bytes);
            if(attempt.successful()) {
                return attempt;
            }
        }

        return Attempt.fail("Could not read certificate data from binary data");
    }

    public void write(final Certificate certificate, final Path path) {
        // todo: catch/check errors with path/files

        // write cert
        try(final OutputStream stream = Files.newOutputStream(path);) {
            this.write(certificate, stream);
        } catch (IOException e) {
            // todo: handle error
            e.printStackTrace();
        }
    }

    public void write(final Certificate certificate, final OutputStream stream) {
        // unwrap cert
        final X509CertificateHolder holder = certificate.unwrap();

        // write (unwrapped object)
        try(
            final Writer writer = new OutputStreamWriter(stream);
            final JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
        ) {
            pemWriter.writeObject(holder);
        } catch (IOException e) {
            // todo: log error
            e.printStackTrace();
        }
    }

}
