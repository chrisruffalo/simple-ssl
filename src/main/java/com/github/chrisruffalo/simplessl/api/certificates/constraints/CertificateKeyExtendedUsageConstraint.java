package com.github.chrisruffalo.simplessl.api.certificates.constraints;

/**
 * Created by cruffalo on 5/19/15.
 */
public class CertificateKeyExtendedUsageConstraint<T> extends X509ConstraintImpl<T> {

    // extensions
    public static final CertificateKeyExtendedUsageConstraint<Void> SERVER_AUTH = new CertificateKeyExtendedUsageConstraint<>("serverAuth", "");
    public static final CertificateKeyExtendedUsageConstraint<Void> CLIENT_AUTH = new CertificateKeyExtendedUsageConstraint<>("clientAuth", "");
    public static final CertificateKeyExtendedUsageConstraint<Void> CODE_SIGNING = new CertificateKeyExtendedUsageConstraint<>("codeSigning", "");
    public static final CertificateKeyExtendedUsageConstraint<Void> EMAIL_PROTECTION = new CertificateKeyExtendedUsageConstraint<>("emailProtection", "");
    public static final CertificateKeyExtendedUsageConstraint<Void> TIME_STAMPING = new CertificateKeyExtendedUsageConstraint<>("timeStamping", "");

    // microsoft extensions
    public static final CertificateKeyExtendedUsageConstraint<Void> MS_INDIVIDUAL_CODE_SIGNING = new CertificateKeyExtendedUsageConstraint<>("msCodeInd", "");
    public static final CertificateKeyExtendedUsageConstraint<Void> MS_COMERCIAL_CODE_SIGNING = new CertificateKeyExtendedUsageConstraint<>("msCodeCom", "");
    public static final CertificateKeyExtendedUsageConstraint<Void> MS_TRUST_LIST_SIGNING = new CertificateKeyExtendedUsageConstraint<>("msCTLSign", "");
    public static final CertificateKeyExtendedUsageConstraint<Void> MS_SERVER_GATED_CRYPTO = new CertificateKeyExtendedUsageConstraint<>("msSGC", "");
    public static final CertificateKeyExtendedUsageConstraint<Void> MS_ENCRYPTED_FILE_SYSTEM = new CertificateKeyExtendedUsageConstraint<>("msEFS", "");

    // netscape extension
    public static final CertificateKeyExtendedUsageConstraint<Void> NS_SERVER_GATED_CRYPTO = new CertificateKeyExtendedUsageConstraint<>("nsSGC", "");


    private CertificateKeyExtendedUsageConstraint(String name, String oid) {
        super(name, oid);
    }
}
