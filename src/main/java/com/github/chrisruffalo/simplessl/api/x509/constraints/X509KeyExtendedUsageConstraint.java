package com.github.chrisruffalo.simplessl.api.x509.constraints;

/**
 * Created by cruffalo on 5/19/15.
 */
public class X509KeyExtendedUsageConstraint<T> extends X509ConstraintImpl<T> {

    // extensions
    public static final X509KeyExtendedUsageConstraint<Void> SERVER_AUTH = new X509KeyExtendedUsageConstraint<>("serverAuth", "");
    public static final X509KeyExtendedUsageConstraint<Void> CLIENT_AUTH = new X509KeyExtendedUsageConstraint<>("clientAuth", "");
    public static final X509KeyExtendedUsageConstraint<Void> CODE_SIGNING = new X509KeyExtendedUsageConstraint<>("codeSigning", "");
    public static final X509KeyExtendedUsageConstraint<Void> EMAIL_PROTECTION = new X509KeyExtendedUsageConstraint<>("emailProtection", "");
    public static final X509KeyExtendedUsageConstraint<Void> TIME_STAMPING = new X509KeyExtendedUsageConstraint<>("timeStamping", "");

    // microsoft extensions
    public static final X509KeyExtendedUsageConstraint<Void> MS_INDIVIDUAL_CODE_SIGNING = new X509KeyExtendedUsageConstraint<>("msCodeInd", "");
    public static final X509KeyExtendedUsageConstraint<Void> MS_COMERCIAL_CODE_SIGNING = new X509KeyExtendedUsageConstraint<>("msCodeCom", "");
    public static final X509KeyExtendedUsageConstraint<Void> MS_TRUST_LIST_SIGNING = new X509KeyExtendedUsageConstraint<>("msCTLSign", "");
    public static final X509KeyExtendedUsageConstraint<Void> MS_SERVER_GATED_CRYPTO = new X509KeyExtendedUsageConstraint<>("msSGC", "");
    public static final X509KeyExtendedUsageConstraint<Void> MS_ENCRYPTED_FILE_SYSTEM = new X509KeyExtendedUsageConstraint<>("msEFS", "");

    // netscape extension
    public static final X509KeyExtendedUsageConstraint<Void> NS_SERVER_GATED_CRYPTO = new X509KeyExtendedUsageConstraint<>("nsSGC", "");


    private X509KeyExtendedUsageConstraint(String name, String oid) {
        super(name, oid);
    }
}
