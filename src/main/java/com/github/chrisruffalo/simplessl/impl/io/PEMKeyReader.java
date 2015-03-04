package com.github.chrisruffalo.simplessl.impl.io;

import com.github.chrisruffalo.simplessl.api.keys.Key;
import com.google.common.base.Optional;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by cruffalo on 2/26/15.
 */
public class PEMKeyReader extends BaseKeyReader {

    @Override
    public <K extends Key> Optional<K> read(InputStream stream) {
        // create source reader from input stream
        try(final Reader sourceReader = new InputStreamReader(stream)) {
            // create pem reader
            final PEMParser parser = new PEMParser(sourceReader);
            try {
                // get decoded/parsed object from reader
                final Object decoded = parser.readObject();
                if(decoded != null) { // null indicates not a PEM file
                    if(decoded instanceof PEMKeyPair) {
                        // get pem pair from decoded file
                        final PEMKeyPair pemPair = (PEMKeyPair) decoded;

                        // convert
                        final JcaPEMKeyConverter pemKeyConverter = new JcaPEMKeyConverter();

                        // create KeyPair from info
                        final KeyPair pair = pemKeyConverter.getKeyPair(pemPair);

                        // get private key if it exists
                        final PrivateKey privateKey = pair.getPrivate();
                        return (Optional<K>)this.wrapPrivate(privateKey);
                    } else if(decoded instanceof SubjectPublicKeyInfo) {
                        final SubjectPublicKeyInfo info = (SubjectPublicKeyInfo)decoded;
                        final JcaPEMKeyConverter pemKeyConverter = new JcaPEMKeyConverter();

                        // get private key and return a wrapped one
                        final PublicKey key = pemKeyConverter.getPublicKey(info);
                        return (Optional<K>)this.wrapPublic(key);
                    } else {
                        this.logger().info("File was PEM encoded but not a key file (type: {})", decoded.getClass().getName());
                    }
                    return Optional.absent();
                }
            } catch (IOException e) {
                this.logger().error("Failed to open object as PEM: {}", e.getMessage());
            }
        } catch (IOException e) {
            this.logger().error("Could not open key file: {}", e.getMessage());
        }

        // return absent optional
        return Optional.absent();
    }
}
