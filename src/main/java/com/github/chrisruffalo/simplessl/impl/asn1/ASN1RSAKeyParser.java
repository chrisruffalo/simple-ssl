package com.github.chrisruffalo.simplessl.impl.asn1;

import com.github.chrisruffalo.simplessl.api.SupportedKeyType;
import com.github.chrisruffalo.simplessl.engine.Provider;
import com.google.common.base.Optional;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by cruffalo on 2/25/15.
 */
public class ASN1RSAKeyParser implements ASN1KeyParser {

    public Optional<KeyPair> parse(ASN1Sequence sequence) {
        // get values from asn1 sequence
        final ASN1Integer mod = (ASN1Integer) sequence.getObjectAt(1);
        final ASN1Integer pubExp = (ASN1Integer) sequence.getObjectAt(2);
        final ASN1Integer privExp = (ASN1Integer) sequence.getObjectAt(3);
        final ASN1Integer primeP = (ASN1Integer) sequence.getObjectAt(4);
        final ASN1Integer primeQ = (ASN1Integer) sequence.getObjectAt(5);
        final ASN1Integer primeExponentP = (ASN1Integer) sequence.getObjectAt(6);
        final ASN1Integer primeExponentQ = (ASN1Integer) sequence.getObjectAt(7);
        final ASN1Integer crtCoef = (ASN1Integer) sequence.getObjectAt(8);

        // spec for public key from sequence
        final RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(mod.getValue(), pubExp.getValue());

        // spec for private key from sequence
        final RSAPrivateKeySpec privSpec = new RSAPrivateCrtKeySpec(
            mod.getValue(),
            pubExp.getValue(),
            privExp.getValue(),
            primeP.getValue(),
            primeQ.getValue(),
            primeExponentP.getValue(),
            primeExponentQ.getValue(),
            crtCoef.getValue()
        );

        // get key factory
        final KeyFactory rsaKeyFactory = Provider.getKeyFactory(SupportedKeyType.RSA);

        try {
            // create private and public keys from the specification
            final PrivateKey privateKey = rsaKeyFactory.generatePrivate(privSpec);
            final PublicKey publicKey = rsaKeyFactory.generatePublic(pubSpec);

            // combine into keypair
            final KeyPair pair = new KeyPair(publicKey, privateKey);

            // return
            return Optional.of(pair);
        } catch (InvalidKeySpecException e) {
            // todo: log error
        }

        return Optional.absent();
    }

}
