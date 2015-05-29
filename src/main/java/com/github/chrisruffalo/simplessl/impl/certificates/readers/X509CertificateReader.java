package com.github.chrisruffalo.simplessl.impl.certificates.readers;

import com.github.chrisruffalo.simplessl.api.certificates.Certificate;
import com.github.chrisruffalo.simplessl.api.model.Attempt;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Created by cruffalo on 2/26/15.
 */
public interface X509CertificateReader {

    <T extends Certificate> Attempt<T> read(final Path path);

    <T extends Certificate> Attempt<T> read(final InputStream stream);

    <T extends Certificate> Attempt<T> read(final byte[] bytes);

}
