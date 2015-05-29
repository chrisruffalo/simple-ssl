package com.github.chrisruffalo.simplessl.api.x509.constraints;

/**
 * Created by cruffalo on 5/19/15.
 */
public class X509KeyUsageConstraint<T> extends X509ConstraintImpl<T> {

    public static final X509KeyUsageConstraint<Void> DIGITAL_SIGNATURE = new X509KeyUsageConstraint<>("digitalSignature", "");
    public static final X509KeyUsageConstraint<Void> NON_REPUDIATION = new X509KeyUsageConstraint<>("nonRepudiation", "");
    public static final X509KeyUsageConstraint<Void> KEY_ENCIPHERMENT = new X509KeyUsageConstraint<>("keyEncipherment", "");
    public static final X509KeyUsageConstraint<Void> DATA_ENCIPHERMENT = new X509KeyUsageConstraint<>("dataEncipherment", "");
    public static final X509KeyUsageConstraint<Void> KEY_AGREEMENT = new X509KeyUsageConstraint<>("keyAgreement", "");
    public static final X509KeyUsageConstraint<Void> KEY_CERT_SIGN = new X509KeyUsageConstraint<>("keyCertSign", "");
    public static final X509KeyUsageConstraint<Void> CRL_SIGN = new X509KeyUsageConstraint<>("cRLSign", "");
    public static final X509KeyUsageConstraint<Void> ENCIPHER_ONLY = new X509KeyUsageConstraint<>("encipherOnly", "");
    public static final X509KeyUsageConstraint<Void> DECIPHER_ONLY = new X509KeyUsageConstraint<>("decipherOnly", "");

    private X509KeyUsageConstraint(String name, String oid) {
        super(name, oid);
    }
}
