package com.github.chrisruffalo.simplessl.api.certificates;

import com.github.chrisruffalo.simplessl.api.certificates.constraints.X509Constraint;

/**
 * Supports x509v3 extensions: https://www.openssl.org/docs/apps/x509v3_config.html
 */
public interface ExtendedCertificateBuilder extends CertificateBuilder {

    ExtendedCertificateBuilder useAsCA(final boolean useAsCA);

    ExtendedCertificateBuilder constrain(final X509Constraint<Void> constraint);
    ExtendedCertificateBuilder constrain(final X509Constraint<Void> constraint, final boolean critical);
    <T> ExtendedCertificateBuilder constrain(final X509Constraint<T> constraint, final T value);
    <T> ExtendedCertificateBuilder constrain(final X509Constraint<T> constraint, final T value, final boolean critical);

    <T> ExtendedCertificateBuilder remove(final X509Constraint<T> constraint);
    ExtendedCertificateBuilder remove(final String constraintName);

}
