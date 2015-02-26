package com.github.chrisruffalo.simplessl;

import com.github.chrisruffalo.simplessl.impl.asn1.ASN1DSAKeyParser;
import com.github.chrisruffalo.simplessl.impl.asn1.ASN1KeyParser;
import com.github.chrisruffalo.simplessl.impl.asn1.ASN1RSAKeyParser;
import com.github.chrisruffalo.simplessl.impl.PrivateKeyImpl;
import com.github.chrisruffalo.simplessl.impl.PublicKeyImpl;
import com.google.common.base.Optional;
import com.google.common.io.ByteStreams;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1StreamParser;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by cruffalo on 2/25/15.
 */
public final class Keys {

    private static final Logger LOGGER = LoggerFactory.getLogger(Keys.class);

    // since this is a utility class nobody should create it at all
    private Keys() {

    }

    /**
     * Reads a path to return a key (private or public) from a
     * PCKS#1 file formatted key (PEM or DER).
     *
     * @param path to the key file (private or public)
     * @return optional Key
     */
    public static <K extends Key> Optional<K> read(Path path) {
        // a path must be provided
        if(path == null) {
            Keys.LOGGER.error("Cannot open null path");
            return Optional.absent();
        }

        // normalize path
        path = path.normalize();

        // make sure it exists
        if(!Files.exists(path)) {
            Keys.LOGGER.error("File at path: '{}' does not exist", path.toString());
            return Optional.absent();
        }

        // make sure it is a file and readable
        if(!Files.isRegularFile(path) || !Files.isReadable(path)) {
            Keys.LOGGER.error("File at path: '{}' must be a readable file", path.toString());
            return Optional.absent();
        }

        // read into a byte stream
        try(final InputStream fileInputStream = Files.newInputStream(path)) {
            // buffer input stream
            InputStream from = new BufferedInputStream(fileInputStream);

            // create source reader from input stream
            Reader sourceReader = new InputStreamReader(from);

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
                        if (privateKey != null) {
                            final com.github.chrisruffalo.simplessl.PrivateKey wrap = new PrivateKeyImpl(privateKey);
                            return Optional.of((K) wrap);
                        }
                    } else if(decoded instanceof SubjectPublicKeyInfo) {
                        final SubjectPublicKeyInfo info = (SubjectPublicKeyInfo)decoded;
                        final JcaPEMKeyConverter pemKeyConverter = new JcaPEMKeyConverter();

                        // get private key and return a wrapped one
                        final PublicKey key = pemKeyConverter.getPublicKey(info);
                        if(key != null) {
                            final com.github.chrisruffalo.simplessl.PublicKey wrap = new PublicKeyImpl(key);
                            return Optional.of((K)wrap);
                        }
                    } else {
                        Keys.LOGGER.info("File ('{}') was PEM encoded but not a key file (type: {})", path, decoded.getClass().getName());
                    }
                    return Optional.absent();
                }
            } catch (IOException e) {
                Keys.LOGGER.error("Failed to open object as PEM: {}", e.getMessage());
            }
        } catch (IOException e) {
            Keys.LOGGER.error("Could not open key file: {}", e.getMessage());

            // return absent optional
            return Optional.absent();
        }

        // read into a byte stream
        try(final InputStream fileInputStream = Files.newInputStream(path)) {
            // buffer input stream
            InputStream from = new BufferedInputStream(fileInputStream);

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
                    Keys.LOGGER.trace("asn1 sequence size: {}", seqSize);

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
                        Keys.LOGGER.info("File ('{}') did not provide enough ASN.1 data for an RSA/DSA key", path);
                    }
                } else {
                    Keys.LOGGER.info("File ('{}') did not provide ASN.1 data", path);
                }
            } catch (IOException e) {
                // couldn't read object
                // but what do?
                Keys.LOGGER.error("Error opening object: {}", e.getMessage(), e);
            }

        } catch (IOException e) {
            Keys.LOGGER.error("Could not open key file: {}", e.getMessage());

            // return absent optional
            return Optional.absent();
        }

        return Optional.absent();
    }


    public static void writePem(Key key, Path path) {
        Keys.writePem(key, null, path);
    }

    public static void writePem(Key key, OutputStream stream) {
        Keys.writePem(key, null, stream);
    }

    public static void writePem(Key key, Cipher cipher, Path path) {
        Keys.write(key.pem(), cipher, path);
    }

    public static void writePem(Key key, Cipher cipher, OutputStream stream) {
        Keys.write(key.pem(), cipher, stream);
    }

    public static void writeDer(Key key, Path path) {
        Keys.writeDer(key, null, path);
    }

    public static void writeDer(Key key, OutputStream stream) {
        Keys.writeDer(key, null, stream);
    }

    public static void writeDer(Key key, Cipher cipher, Path path) {
        Keys.write(key.der(), cipher, path);
    }

    public static void writeDer(Key key, Cipher cipher, OutputStream stream) {
        Keys.write(key.der(), cipher, stream);
    }

    private static void write(byte[] bytes, Cipher cipher, Path path) {
        // todo : catch errors with path / files

        // write
        try (final OutputStream stream = Files.newOutputStream(path)) {
            Keys.write(bytes, cipher, stream);
        } catch (IOException e) {
            // todo: log
        }
    }

    private static void write(byte[] bytes, Cipher cipher, OutputStream stream) {
        try(final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            ByteStreams.copy(inputStream, stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
