package com.github.chrisruffalo.simplessl.impl.certificates;

import com.github.chrisruffalo.simplessl.api.SupportedSignatureType;
import com.github.chrisruffalo.simplessl.api.certificates.Certificate;
import com.github.chrisruffalo.simplessl.api.certificates.CertificateBuilder;
import com.github.chrisruffalo.simplessl.api.certificates.ExtendedCertificateBuilder;
import com.github.chrisruffalo.simplessl.engine.Provider;
import com.github.chrisruffalo.simplessl.impl.keys.PublicKeyImpl;
import com.google.common.base.Optional;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * Created by cruffalo on 2/26/15.
 */
public class CertificateBuilderImpl implements CertificateBuilder, ExtendedCertificateBuilder {

    private SupportedSignatureType signatureType;

    private String issuerName;

    private String subjectName;

    private BigInteger version = BigInteger.ONE;

    private Date startDate;

    private Date endDate;

    private PrivateKey privateKey;

    private PublicKey publicKey;

    // extension data
    private boolean isCa = false;


    public CertificateBuilderImpl() {
        // certificate starts today and lasts one year, by default
        this.startDate = new Date(); // now
        this.endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000); // one year

        // default subject and issuer
        this.issuerName = "dn=default_issuer";
        this.subjectName = "dn=default_subject";

        // supported signature type
        this.signatureType = SupportedSignatureType.RSA_SHA256;
    }

    @Override
    public CertificateBuilder setPrivateKey(java.security.PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    @Override
    public CertificateBuilder setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    @Override
    public CertificateBuilder startDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    @Override
    public CertificateBuilder endDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    @Override
    public ExtendedCertificateBuilder withExtensions() {
        // v3 certs have extensions
        this.version = BigInteger.valueOf(3);
        return this;
    }

    @Override
    public ExtendedCertificateBuilder useAsCA(boolean useAsCA) {
        this.isCa = useAsCA;
        return this;
    }

    @Override
    public Optional<Certificate> build() {
        // X500 Name
        final X500Name issuerName = new X500Name(this.issuerName);
        final X500Name subjectName = new X500Name(this.subjectName);

        // signs the content
        final ContentSigner signer = Provider.getContentSigner(this.signatureType, this.privateKey);

        // public key (unwrap if possible, just for lower-level support, even though the decorator should work)
        PublicKey pKey = this.publicKey;
        if(pKey instanceof com.github.chrisruffalo.simplessl.api.keys.PublicKey) {
            pKey = ((com.github.chrisruffalo.simplessl.api.keys.PublicKey) pKey).unwrap();
        }

        // public key info, the public key info for the owner of the cert
        final SubjectPublicKeyInfo info = new PublicKeyImpl(pKey).info();

        // choose builder based on version / extended attributes
        if(this.version == BigInteger.valueOf(3)) {
            return Optional.of(this.buildV3(issuerName, signer, subjectName, info));
        }
        return Optional.of(this.buildV1(issuerName, signer, subjectName, info));
    }

    private Certificate buildV1(X500Name issuerName, ContentSigner signer, X500Name subjectName, SubjectPublicKeyInfo info) {
        final X509v1CertificateBuilder builder = new X509v1CertificateBuilder(issuerName, BigInteger.ONE, this.startDate, this.endDate, subjectName, info);

        final X509CertificateHolder holder = builder.build(signer);
        final CertificateImpl impl = new CertificateImpl(holder);

        return impl;
    }

    private Certificate buildV3(X500Name issuerName, ContentSigner signer, X500Name subjectName, SubjectPublicKeyInfo info) {
        final X509v3CertificateBuilder builder = new X509v3CertificateBuilder(issuerName, BigInteger.ONE, this.startDate, this.endDate, subjectName, info);

        final X509CertificateHolder holder = builder.build(signer);
        final CertificateImpl impl = new CertificateImpl(holder);

        return impl;
    }
}
