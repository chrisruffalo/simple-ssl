package com.github.chrisruffalo.simplessl.impl.certificates.readers;

import com.github.chrisruffalo.simplessl.api.certificates.Certificate;
import com.github.chrisruffalo.simplessl.api.keys.Key;
import com.github.chrisruffalo.simplessl.api.model.Attempt;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Created by cruffalo on 2/26/15.
 */
public interface X509CertificateReader {

    Attempt<Certificate> read(Path path);

    Attempt<Certificate> read(InputStream stream);

    Attempt<Certificate> read(byte[] bytes);

}
