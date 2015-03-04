package com.github.chrisruffalo.simplessl.impl.certificates;

import com.github.chrisruffalo.simplessl.api.certificates.Certificate;
import org.bouncycastle.cert.X509CertificateHolder;

/**
 * Created by cruffalo on 2/26/15.
 */
public class CertificateImpl implements Certificate {

    private final X509CertificateHolder holder;

    public CertificateImpl(X509CertificateHolder holder) {
        this.holder = holder;
    }

    @Override
    public X509CertificateHolder unwrap() {
        return this.holder;
    }
}
