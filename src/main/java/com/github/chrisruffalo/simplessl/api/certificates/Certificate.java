package com.github.chrisruffalo.simplessl.api.certificates;

import org.bouncycastle.cert.X509CertificateHolder;

/**
 * Created by cruffalo on 2/26/15.
 */
public interface Certificate {

    X509CertificateHolder unwrap();
}
