package com.github.chrisruffalo.simplessl.impl.asn1;

import com.github.chrisruffalo.simplessl.api.model.Attempt;
import org.bouncycastle.asn1.ASN1Sequence;

import java.security.KeyPair;

/**
 * Created by cruffalo on 2/25/15.
 */
public interface ASN1KeyParser {

    // get key pair
    Attempt<KeyPair> parse(ASN1Sequence sequence);

}
