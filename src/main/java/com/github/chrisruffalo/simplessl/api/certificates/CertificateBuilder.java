package com.github.chrisruffalo.simplessl.api.certificates;

import com.github.chrisruffalo.simplessl.api.model.Attempt;

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

    Attempt<Certificate> build();

}
