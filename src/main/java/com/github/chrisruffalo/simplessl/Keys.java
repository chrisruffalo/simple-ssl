package com.github.chrisruffalo.simplessl;

import com.github.chrisruffalo.simplessl.api.keys.Key;
import com.github.chrisruffalo.simplessl.api.keys.rsa.RSAKeyPair;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.impl.io.DERKeyReader;
import com.github.chrisruffalo.simplessl.impl.io.KeyReader;
import com.github.chrisruffalo.simplessl.impl.io.PEMKeyReader;
import com.github.chrisruffalo.simplessl.impl.keys.rsa.RSAKeyPairImpl;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cruffalo on 2/25/15.
 */
public final class Keys {

    private static final Logger LOGGER = LoggerFactory.getLogger(Keys.class);

    private static final List<KeyReader> READER_CHAIN = new LinkedList<>();

    // initialize reader chain
    static {
        // try pem first
        Keys.READER_CHAIN.add(new PEMKeyReader());
        // them try der
        Keys.READER_CHAIN.add(new DERKeyReader());
    }

    // since this is a utility class nobody should create it at all
    private Keys() {

    }

    /**
     * Generate an RSA key pair.
     *
     * @return generated key pair
     */
    public static RSAKeyPair generateRSA() {
        return RSAKeyPairImpl.generate();
    }

    /**
     * Generate an RSA key pair, specifying the bit size.  The
     * bit size should be at least 2048.
     *
     * @param bits size of the key in bits, should be at least 2048
     * @return generated key pair
     */
    public static RSAKeyPair generateRSA(int bits) {
        return RSAKeyPairImpl.generate(bits);
    }

    /**
     * Generate an RSA key pair, specifying the bit size.  The bit
     * size should be at least 2048.  The exponent should
     * only be changed if you know what you are doing.
     *
     * @param bits size of the key in bits, should be at least 2048
     * @param exponent the key's exponent
     * @return
     */
    public static RSAKeyPair generateRSA(int bits, BigInteger exponent) {
        return RSAKeyPairImpl.generate(bits, exponent);
    }

    /**
     * Reads a path to return a key (private or public) from a
     * PCKS#1 file formatted key (PEM or DER).
     *
     * @param path to the key file (private or public)
     * @return optionally the Key read from the file
     */
    public static <K extends Key> Attempt<K> read(Path path) {
        // a path must be provided
        if(path == null) {
            return Attempt.fail("Cannot open null path");
        }

        // normalize path
        path = path.normalize();

        // make sure it exists
        if(!Files.exists(path)) {
            return Attempt.fail(String.format("File at path: '%s' does not exist", path.toString()));
        }

        // make sure it is a file and readable
        if(!Files.isRegularFile(path) || !Files.isReadable(path)) {
            return Attempt.fail(String.format("File at path: '%s' must be a readable file", path.toString()));
        }

        // make sure file has data, otherwise will probably choke on subsequent methods anyway
        try {
            // error out if the file has no data
            if(Files.size(path) < 1) {
                return Attempt.fail(String.format("File at '%s' has no content", path.toString()));
            }
        } catch (IOException e) {
            // return error because there is nothing to do, further reads would probably get I/O error or otherwise choke
            return Attempt.fail(String.format("Could not determine size of file at '%s'", path.toString()), e);
        }

        // read file
        try(InputStream stream = Files.newInputStream(path)) {
            return Keys.read(stream);
        } catch (IOException e) {
            return Attempt.fail(String.format("IO exception while reading file at '%s', error: %s", path.toString(), e.getMessage()), e);
        }
    }

    /**
     * Read key from byte array.  Will read PEM or DER format.
     *
     * @param input source byte array
     * @return optionally the Key read from the byte array
     */
    public static <K extends Key> Attempt<K> read(byte[] input) {
        // can't read null or empty bytes
        if(input == null || input.length < 1) {
            return Attempt.fail("Cannot read from empty or null byte array");
        }

        for(KeyReader reader : Keys.READER_CHAIN) {
            final Attempt<K> keyFound = reader.read(input);
            if(keyFound.successful()) {
                return keyFound;
            }
        }

        // unspecified error, no key found
        return Attempt.fail("No key found in input data");
    }

    /**
     *Read key from input stream.  Will read PEM or DER format.
     *
     * @param inputStream the source input stream for the key
     * @return optionally the Key read from the byte array
     */
    public static <K extends Key> Attempt<K> read(InputStream inputStream) {

        // can't read null stream
        if(inputStream == null) {
            return Attempt.fail("Cannot read key from a null stream");
        }

        // read input stream to bytes
        final byte[] bytes;
        try {
            bytes = ByteStreams.toByteArray(inputStream);
        } catch (IOException e) {
            return Attempt.fail("Could not read stream");
        }

        return Keys.read(bytes);
    }

    public static void writePEM(Key key, Path path) {
        Keys.writePEM(key, null, path);
    }

    public static void writePEM(Key key, OutputStream stream) {
        Keys.writePEM(key, null, stream);
    }

    public static void writePEM(Key key, Cipher cipher, Path path) {
        Keys.write(key.pem(), cipher, path);
    }

    public static void writePEM(Key key, Cipher cipher, OutputStream stream) {
        Keys.write(key.pem(), cipher, stream);
    }

    public static void writeDER(Key key, Path path) {
        Keys.writeDER(key, null, path);
    }

    public static void writeDER(Key key, OutputStream stream) {
        Keys.writeDER(key, null, stream);
    }

    public static void writeDER(Key key, Cipher cipher, Path path) {
        Keys.write(key.der(), cipher, path);
    }

    public static void writeDER(Key key, Cipher cipher, OutputStream stream) {
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
