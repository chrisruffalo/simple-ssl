package com.github.chrisruffalo.simplessl.impl.x509;

import com.github.chrisruffalo.simplessl.api.x509.SimpleX509Certificate;
import org.bouncycastle.cert.X509CertificateHolder;

/**
 * Created by cruffalo on 2/26/15.
 */
public class SimpleX509CertificateImpl implements SimpleX509Certificate {

    private final X509CertificateHolder holder;

    public SimpleX509CertificateImpl(final X509CertificateHolder holder) {
        if(holder == null) {
            throw new IllegalStateException("A certificate instance cannot be backed by a null container");
        }
        this.holder = holder;
    }

    @Override
    public X509CertificateHolder unwrap() {
        return this.holder;
    }
}
