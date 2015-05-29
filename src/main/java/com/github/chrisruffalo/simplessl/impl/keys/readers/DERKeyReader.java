package com.github.chrisruffalo.simplessl.impl.keys.readers;

import com.github.chrisruffalo.simplessl.api.keys.SimpleKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.api.model.Error;
import com.github.chrisruffalo.simplessl.impl.asn1.ASN1DSAKeyParser;
import com.github.chrisruffalo.simplessl.impl.asn1.ASN1KeyParser;
import com.github.chrisruffalo.simplessl.impl.asn1.ASN1RSAKeyParser;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1StreamParser;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cruffalo on 2/26/15.
 */
public class DERKeyReader extends BaseKeyReader {

    @Override
    public <K extends SimpleKey> Attempt<K> read(final InputStream stream) {
        List<Error> errors = new LinkedList<>();

        // read into a byte stream
        try (final InputStream from = new BufferedInputStream(stream);) {
            // create new input stream from buffered file input stream
            ASN1StreamParser asn1Parser = new ASN1StreamParser(from);
            try {
                ASN1Encodable encoded = asn1Parser.readObject();
                ASN1Primitive primitive = encoded.toASN1Primitive();

                // ASN1 sequence data is required to build the info
                // for the private or public key
                if (primitive instanceof ASN1Sequence) {
                    // get sequence
                    final ASN1Sequence sequence = (ASN1Sequence) primitive;

                    // save size
                    final int seqSize = sequence.size();

                    // check sizes
                    if (seqSize >= 5) { // assume "RSA" private key type
                        // decide on key parser based on sequence size, assume DSA for >=5 and RSA for >= 8
                        final ASN1KeyParser asn1KeyParser = seqSize >= 8 ? new ASN1RSAKeyParser() : new ASN1DSAKeyParser();

                        // parse pair and create output
                        final Attempt<KeyPair> parsedPairOption = asn1KeyParser.parse(sequence);

                        // check for a return value
                        if (!parsedPairOption.successful()) {
                            return Attempt.fail(parsedPairOption.errors(), parsedPairOption.warnings());
                        }
                        final KeyPair parsedPair = parsedPairOption.get();

                        // get private key and return a wrapped version
                        final PrivateKey privateKey = parsedPair.getPrivate();
                        return (Attempt<K>) this.wrapPrivate(privateKey);
                    } else if (seqSize > 0) { // assume public key (RSA or DSA figured by SubjectPublicKeyInfo)
                        final SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(sequence);
                        final JcaPEMKeyConverter pemKeyConverter = new JcaPEMKeyConverter();

                        // get private key and return a wrapped one
                        final PublicKey key = pemKeyConverter.getPublicKey(info);
                        return (Attempt<K>) this.wrapPublic(key);
                    } else {
                        errors.add(new Error("File did not provide enough ASN.1 data for an RSA/DSA key"));
                    }
                } else {
                    errors.add(new Error("File did not provide ASN.1 data"));
                }
            } catch (IOException e) {
                return Attempt.fail("Error opening object: " + e.getMessage(), e);
            }
        } catch (IOException e) {
            return Attempt.fail("Could not open key file: " + e.getMessage(), e);
        }

        // return absent optional
        errors.add(new Error("Could not parse DER data from given input"));
        return Attempt.fail(errors);
    }
}
