package com.github.chrisruffalo.simplessl;

import com.github.chrisruffalo.simplessl.api.certificates.CertificateBuilder;
import com.github.chrisruffalo.simplessl.impl.certificates.CertificateBuilderImpl;

/**
 * Created by cruffalo on 2/26/15.
 */
public final class Certificates {

    private Certificates() {

    }

    public static CertificateBuilder builder() {
        return new CertificateBuilderImpl();
    }

}
