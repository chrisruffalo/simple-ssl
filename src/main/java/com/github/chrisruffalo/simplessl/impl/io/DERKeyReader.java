package com.github.chrisruffalo.simplessl.impl.io;

import com.github.chrisruffalo.simplessl.Key;
import com.github.chrisruffalo.simplessl.impl.PrivateKeyImpl;
import com.github.chrisruffalo.simplessl.impl.PublicKeyImpl;
import com.github.chrisruffalo.simplessl.impl.asn1.ASN1DSAKeyParser;
import com.github.chrisruffalo.simplessl.impl.asn1.ASN1KeyParser;
import com.github.chrisruffalo.simplessl.impl.asn1.ASN1RSAKeyParser;
import com.google.common.base.Optional;
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

/**
 * Created by cruffalo on 2/26/15.
 */
public class DERKeyReader extends BaseKeyReader {

    @Override
    public <K extends Key> Optional<K> read(InputStream stream) {
        // read into a byte stream
        try(final InputStream from = new BufferedInputStream(stream);) {
            // create new input stream from buffered file input stream
            ASN1StreamParser asn1Parser = new ASN1StreamParser(from);
            try {
                ASN1Encodable encoded = asn1Parser.readObject();
                ASN1Primitive primitive = encoded.toASN1Primitive();

                // ASN1 sequence data is required to build the info
                // for the private or public key
                if(primitive instanceof ASN1Sequence) {
                    // get sequence
                    final ASN1Sequence sequence = (ASN1Sequence)primitive;

                    // save size
                    final int seqSize = sequence.size();

                    // say size
                    this.logger().trace("asn1 sequence size: {}", seqSize);

                    // check sizes
                    if(seqSize >= 5) { // assume "RSA" private key type
                        // decide on key parser based on sequence size, assume DSA for >=5 and RSA for >= 8
                        final ASN1KeyParser asn1KeyParser = seqSize >= 8 ? new ASN1RSAKeyParser() : new ASN1DSAKeyParser();

                        // parse pair and create output
                        final Optional<KeyPair> parsedPairOption = asn1KeyParser.parse(sequence);

                        // check for a return value
                        if(!parsedPairOption.isPresent()) {
                            return Optional.absent();
                        }
                        final KeyPair parsedPair = parsedPairOption.get();

                        final PrivateKey privateKey = parsedPair.getPrivate();
                        if(null != privateKey) {
                            final com.github.chrisruffalo.simplessl.PrivateKey wrap = new PrivateKeyImpl(privateKey);
                            return Optional.of((K) wrap);
                        }
                    } else if(seqSize > 0) { // assume public key (RSA or DSA figured by SubjectPublicKeyInfo)
                        final SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(sequence);
                        final JcaPEMKeyConverter pemKeyConverter = new JcaPEMKeyConverter();

                        // get private key and return a wrapped one
                        final PublicKey key = pemKeyConverter.getPublicKey(info);
                        if(key != null) {
                            final com.github.chrisruffalo.simplessl.PublicKey wrap = new PublicKeyImpl(key);
                            return Optional.of((K)wrap);
                        }
                    } else {
                        this.logger().info("File did not provide enough ASN.1 data for an RSA/DSA key");
                    }
                } else {
                    this.logger().info("File did not provide ASN.1 data");
                }
            } catch (IOException e) {
                // couldn't read object
                // but what do?
                this.logger().error("Error opening object: {}", e.getMessage(), e);
            }
        } catch (IOException e) {
            this.logger().error("Could not open key file: {}", e.getMessage());
        }

        // return absent optional
        return Optional.absent();
    }
}
