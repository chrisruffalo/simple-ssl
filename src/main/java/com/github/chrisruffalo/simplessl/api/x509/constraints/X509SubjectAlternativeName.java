package com.github.chrisruffalo.simplessl.api.x509.constraints;

import com.github.chrisruffalo.simplessl.api.model.x509.DistinguishedName;
import com.github.chrisruffalo.simplessl.api.model.x509.OtherName;

/**
 * Created by cruffalo on 5/19/15.
 */
public class X509SubjectAlternativeName<T> extends X509ConstraintImpl<T> {

    public static final X509SubjectAlternativeName<String> EMAIL = new X509SubjectAlternativeName<>("email", "");
    public static final X509SubjectAlternativeName<java.net.URI> URI = new X509SubjectAlternativeName<>("URI", "");
    public static final X509SubjectAlternativeName<String> DNS = new X509SubjectAlternativeName<>("DNS", "");
    public static final X509SubjectAlternativeName<String> RID = new X509SubjectAlternativeName<>("RID", "");
    public static final X509SubjectAlternativeName<String> IP = new X509SubjectAlternativeName<>("IP", "");
    public static final X509SubjectAlternativeName<DistinguishedName> DIR_NAME = new X509SubjectAlternativeName<>("dirName", "");
    public static final X509SubjectAlternativeName<OtherName> OTHER_NAME = new X509SubjectAlternativeName<>("otherName", "");

    private X509SubjectAlternativeName(String name, String oid) {
        super(name, oid);
    }
}
