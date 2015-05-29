package com.github.chrisruffalo.simplessl.impl.certificates;

import com.github.chrisruffalo.simplessl.api.SupportedSignatureType;
import com.github.chrisruffalo.simplessl.api.certificates.Certificate;
import com.github.chrisruffalo.simplessl.api.certificates.CertificateBuilder;
import com.github.chrisruffalo.simplessl.api.certificates.ExtendedCertificateBuilder;
import com.github.chrisruffalo.simplessl.api.certificates.constraints.CertificateBasicConstraint;
import com.github.chrisruffalo.simplessl.api.certificates.constraints.X509Constraint;
import com.github.chrisruffalo.simplessl.api.keys.SimplePublicKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.engine.Provider;
import com.github.chrisruffalo.simplessl.impl.keys.SimplePublicKeyImpl;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
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

    private int serial;

    // extension data
    private boolean isCa = false;

    public CertificateBuilderImpl() {
        // certificate starts today and lasts one year, by default
        this.startDate = new Date(); // now
        this.endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000); // one year

        // default subject and issuer
        this.issuerName = "dn=default_issuer";
        this.subjectName = "dn=default_subject";

        // random serial by default
        this.serial = -1;

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
        // returns the extended builder but
        // with no extension data until some is
        // set
        return this;
    }

    @Override
    public ExtendedCertificateBuilder useAsCA(boolean useAsCA) {
        this.isCa = useAsCA;
        if(useAsCA) {
            // set constraint
            this.constrain(CertificateBasicConstraint.CA, true, true);
        } else {
            // remove constraint
            this.remove(CertificateBasicConstraint.CA);
        }
        return this;
    }

    @Override
    public ExtendedCertificateBuilder constrain(X509Constraint<Void> constraint) {
        return null;
    }

    @Override
    public ExtendedCertificateBuilder constrain(X509Constraint<Void> constraint, boolean critical) {
        return null;
    }

    @Override
    public <T> ExtendedCertificateBuilder constrain(X509Constraint<T> constraint, T value) {
        return null;
    }

    @Override
    public <T> ExtendedCertificateBuilder constrain(X509Constraint<T> constraint, T value, boolean critical) {
        return null;
    }

    @Override
    public <T> ExtendedCertificateBuilder remove(X509Constraint<T> constraint) {
        return null;
    }

    @Override
    public ExtendedCertificateBuilder remove(String constraintName) {
        return null;
    }

    @Override
    public Attempt<Certificate> build() {
        if(this.privateKey == null || this.publicKey == null) {
            return Attempt.fail("The private key of the signatory and the public key of the certificate holder are both required.");
        }

        // X500 Name
        final X500Name issuerName = new X500Name(this.issuerName);
        final X500Name subjectName = new X500Name(this.subjectName);

        // signs the content
        final ContentSigner signer = Provider.getContentSigner(this.signatureType, this.privateKey);

        // public key (unwrap if possible, just for lower-level support, even though the decorator should work)
        PublicKey pKey = this.publicKey;
        if(pKey instanceof SimplePublicKey) {
            pKey = ((SimplePublicKey) pKey).unwrap();
        }

        // public key info, the public key info for the owner of the cert
        final SubjectPublicKeyInfo info = new SimplePublicKeyImpl(pKey).info();

        // create serial and generate random if none has been specified
        BigInteger serial = BigInteger.valueOf(this.serial);
        if(this.serial < 0) {
            final SecureRandom random = new SecureRandom();
            final int randomSerial = random.nextInt(Integer.MAX_VALUE);
            serial = BigInteger.valueOf(randomSerial);
        }

        // choose builder based on version / extended attributes
        //if(this.extended) {
        //    return this.buildV3(issuerName, signer, serial, subjectName, info);
        //}
        return this.buildV1(issuerName, signer, serial, subjectName, info);
    }

    private Attempt<Certificate> buildV1(X500Name issuerName, ContentSigner signer, BigInteger serial, X500Name subjectName, SubjectPublicKeyInfo info) {
        final X509v1CertificateBuilder builder = new X509v1CertificateBuilder(issuerName, serial, this.startDate, this.endDate, subjectName, info);

        final X509CertificateHolder holder = builder.build(signer);
        final CertificateImpl impl = new CertificateImpl(holder);

        return Attempt.succeed((Certificate)impl);
    }

    private Attempt<Certificate> buildV3(X500Name issuerName, ContentSigner signer, BigInteger serial, X500Name subjectName, SubjectPublicKeyInfo info) {
        final X509v3CertificateBuilder builder = new X509v3CertificateBuilder(issuerName, BigInteger.ONE, this.startDate, this.endDate, subjectName, info);

        final X509CertificateHolder holder = builder.build(signer);
        final CertificateImpl impl = new CertificateImpl(holder);



        return Attempt.succeed((Certificate)impl);
    }
}
