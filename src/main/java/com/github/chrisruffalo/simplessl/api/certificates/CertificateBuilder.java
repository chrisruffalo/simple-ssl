package com.github.chrisruffalo.simplessl.api.certificates;

import com.google.common.base.Optional;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * Created by cruffalo on 2/26/15.
 */
public interface CertificateBuilder {

    CertificateBuilder setPrivateKey(PrivateKey privateKey);

    CertificateBuilder setPublicKey(PublicKey publicKey);

    CertificateBuilder startDate(Date date);

    CertificateBuilder endDate(Date date);

    ExtendedCertificateBuilder withExtensions();

    Optional<Certificate> build();

}
