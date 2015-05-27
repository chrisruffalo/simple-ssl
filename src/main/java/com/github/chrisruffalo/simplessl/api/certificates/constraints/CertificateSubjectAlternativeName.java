package com.github.chrisruffalo.simplessl.api.certificates.constraints;

import com.github.chrisruffalo.simplessl.api.model.x509.DistinguishedName;
import com.github.chrisruffalo.simplessl.api.model.x509.OtherName;

/**
 * Created by cruffalo on 5/19/15.
 */
public class CertificateSubjectAlternativeName<T> extends X509ConstraintImpl<T> {

    public static final CertificateSubjectAlternativeName<String> EMAIL = new CertificateSubjectAlternativeName<>("email", "");
    public static final CertificateSubjectAlternativeName<java.net.URI> URI = new CertificateSubjectAlternativeName<>("URI", "");
    public static final CertificateSubjectAlternativeName<String> DNS = new CertificateSubjectAlternativeName<>("DNS", "");
    public static final CertificateSubjectAlternativeName<String> RID = new CertificateSubjectAlternativeName<>("RID", "");
    public static final CertificateSubjectAlternativeName<String> IP = new CertificateSubjectAlternativeName<>("IP", "");
    public static final CertificateSubjectAlternativeName<DistinguishedName> DIR_NAME = new CertificateSubjectAlternativeName<>("dirName", "");
    public static final CertificateSubjectAlternativeName<OtherName> OTHER_NAME = new CertificateSubjectAlternativeName<>("otherName", "");

    private CertificateSubjectAlternativeName(String name, String oid) {
        super(name, oid);
    }
}
