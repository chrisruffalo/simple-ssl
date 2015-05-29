package com.github.chrisruffalo.simplessl.api.x509;

import com.github.chrisruffalo.simplessl.api.model.Attempt;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * Created by cruffalo on 2/26/15.
 */
public interface SimpleX509CertificateBuilder {

    SimpleX509CertificateBuilder setPrivateKey(PrivateKey privateKey);

    SimpleX509CertificateBuilder setPublicKey(PublicKey publicKey);

    SimpleX509CertificateBuilder startDate(Date date);

    SimpleX509CertificateBuilder endDate(Date date);

    SimpleX509ExtendedCertificateBuilder extend();

    Attempt<SimpleX509Certificate> build();

}
