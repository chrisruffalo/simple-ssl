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
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by cruffalo on 2/25/15.
 */
public class ASN1DSAKeyParser implements ASN1KeyParser {

    public Optional<KeyPair> parse(ASN1Sequence sequence) {
        // get values from asn1 sequence
        final ASN1Integer p = (ASN1Integer) sequence.getObjectAt(1);
        final ASN1Integer q = (ASN1Integer) sequence.getObjectAt(2);
        final ASN1Integer g = (ASN1Integer) sequence.getObjectAt(3);
        final ASN1Integer y = (ASN1Integer) sequence.getObjectAt(4);
        final ASN1Integer x = (ASN1Integer) sequence.getObjectAt(5);

        // spec for public key from sequence
        final DSAPrivateKeySpec pubSpec = new DSAPrivateKeySpec(y.getValue(), p.getValue(), q.getValue(), g.getValue());

        // spec for private key from sequence
        final DSAPrivateKeySpec privSpec = new DSAPrivateKeySpec(x.getValue(), p.getValue(), q.getValue(), g.getValue());

        // get key factory
        final KeyFactory dsaKeyFactory = Provider.getKeyFactory(SupportedKeyType.DSA);

        try {
            // create private and public keys from the specification
            final PrivateKey privateKey = dsaKeyFactory.generatePrivate(privSpec);
            final PublicKey publicKey = dsaKeyFactory.generatePublic(pubSpec);

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
