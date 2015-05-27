package com.github.chrisruffalo.simplessl.impl.certificates.readers;

import com.github.chrisruffalo.simplessl.api.certificates.Certificate;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.impl.SimpleReader;

import java.io.InputStream;

/**
 * Created by cruffalo on 5/26/15.
 */
public class X509DERCertificateReader extends SimpleReader<Certificate> implements X509CertificateReader {

    @Override
    public Attempt<Certificate> read(InputStream inputStream) {
        return null;
    }
    
}
