package com.github.chrisruffalo.simplessl.api.x509;

import com.github.chrisruffalo.simplessl.api.x509.constraints.X509Constraint;

/**
 * Supports x509v3 extensions: https://www.openssl.org/docs/apps/x509v3_config.html
 */
public interface SimpleX509ExtendedCertificateBuilder extends SimpleX509CertificateBuilder {

    SimpleX509ExtendedCertificateBuilder useAsCA(final boolean useAsCA);

    SimpleX509ExtendedCertificateBuilder constrain(final X509Constraint<Void> constraint);
    SimpleX509ExtendedCertificateBuilder constrain(final X509Constraint<Void> constraint, final boolean critical);
    <T> SimpleX509ExtendedCertificateBuilder constrain(final X509Constraint<T> constraint, final T value);
    <T> SimpleX509ExtendedCertificateBuilder constrain(final X509Constraint<T> constraint, final T value, final boolean critical);

    <T> SimpleX509ExtendedCertificateBuilder remove(final X509Constraint<T> constraint);
    SimpleX509ExtendedCertificateBuilder remove(final String constraintName);

}
