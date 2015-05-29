package com.github.chrisruffalo.simplessl.api.x509;

import org.bouncycastle.cert.X509CertificateHolder;

/**
 * Created by cruffalo on 2/26/15.
 */
public interface SimpleX509Certificate {

    X509CertificateHolder unwrap();

}
