package com.github.chrisruffalo.simplessl.api.certificates;

/**
 * Created by cruffalo on 2/26/15.
 */
public interface ExtendedCertificateBuilder extends CertificateBuilder {

    ExtendedCertificateBuilder useAsCA(boolean useAsCA);

}
