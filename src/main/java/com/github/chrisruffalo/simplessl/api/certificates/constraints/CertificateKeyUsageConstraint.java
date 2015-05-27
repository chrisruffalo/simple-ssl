package com.github.chrisruffalo.simplessl.api.certificates.constraints;

/**
 * Created by cruffalo on 5/19/15.
 */
public class CertificateKeyUsageConstraint<T> extends X509ConstraintImpl<T> {

    public static final CertificateKeyUsageConstraint<Void> DIGITAL_SIGNATURE = new CertificateKeyUsageConstraint<>("digitalSignature", "");
    public static final CertificateKeyUsageConstraint<Void> NON_REPUDIATION = new CertificateKeyUsageConstraint<>("nonRepudiation", "");
    public static final CertificateKeyUsageConstraint<Void> KEY_ENCIPHERMENT = new CertificateKeyUsageConstraint<>("keyEncipherment", "");
    public static final CertificateKeyUsageConstraint<Void> DATA_ENCIPHERMENT = new CertificateKeyUsageConstraint<>("dataEncipherment", "");
    public static final CertificateKeyUsageConstraint<Void> KEY_AGREEMENT = new CertificateKeyUsageConstraint<>("keyAgreement", "");
    public static final CertificateKeyUsageConstraint<Void> KEY_CERT_SIGN = new CertificateKeyUsageConstraint<>("keyCertSign", "");
    public static final CertificateKeyUsageConstraint<Void> CRL_SIGN = new CertificateKeyUsageConstraint<>("cRLSign", "");
    public static final CertificateKeyUsageConstraint<Void> ENCIPHER_ONLY = new CertificateKeyUsageConstraint<>("encipherOnly", "");
    public static final CertificateKeyUsageConstraint<Void> DECIPHER_ONLY = new CertificateKeyUsageConstraint<>("decipherOnly", "");

    private CertificateKeyUsageConstraint(String name, String oid) {
        super(name, oid);
    }
}
