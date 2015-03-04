package com.github.chrisruffalo.simplessl.impl.keys;

import com.github.chrisruffalo.simplessl.api.keys.PublicKey;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

/**
 * Created by cruffalo on 2/25/15.
 */
public class PublicKeyImpl extends KeyImpl implements PublicKey {

    private final java.security.PublicKey wrapped;

    public PublicKeyImpl(java.security.PublicKey key) {
        this.wrapped = key;
    }

    @Override
    public SubjectPublicKeyInfo info() {
        final java.security.PublicKey pKey = this.wrapped;

        SubjectPublicKeyInfo info = null;

        // convert public key as given into a subject key info
        if(pKey instanceof ASN1Sequence) {
            info = new SubjectPublicKeyInfo((ASN1Sequence)pKey);
        } else {
            // attempt default method
            final byte[] encoded = pKey.getEncoded();
            final ASN1Sequence sequence = ASN1Sequence.getInstance(encoded);
            info = new SubjectPublicKeyInfo(sequence);
        }

        return info;
    }

    @Override
    public java.security.PublicKey unwrap() {
        return this.wrapped;
    }

    @Override
    public boolean isPrivate() {
        return false;
    }

    @Override
    public boolean isPublic() {
        return true;
    }
}
