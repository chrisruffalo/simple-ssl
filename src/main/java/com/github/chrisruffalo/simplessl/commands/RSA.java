package com.github.chrisruffalo.simplessl.commands;

import com.github.chrisruffalo.simplessl.api.WriteMode;
import com.github.chrisruffalo.simplessl.api.keys.SimpleKey;
import com.github.chrisruffalo.simplessl.api.keys.rsa.SimpleRSAKeyPair;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.impl.keys.readers.DERKeyReader;
import com.github.chrisruffalo.simplessl.impl.keys.readers.KeyReader;
import com.github.chrisruffalo.simplessl.impl.keys.readers.PEMKeyReader;
import com.github.chrisruffalo.simplessl.impl.keys.rsa.SimpleRSAKeyPairImpl;
import com.google.common.io.ByteStreams;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cruffalo on 2/25/15.
 */
public final class RSA {

    private final List<KeyReader> readerChain = new ArrayList<>(2);

    // since this is a utility class nobody should create it at all
    public RSA() {
        // try pem first
        this.readerChain.add(new PEMKeyReader());
        // them try der
        this.readerChain.add(new DERKeyReader());
    }

    /**
     * Generate an RSA key pair.
     *
     * @return generated key pair
     */
    public SimpleRSAKeyPair generate() {
        return SimpleRSAKeyPairImpl.generate();
    }

    /**
     * Generate an RSA key pair, specifying the bit size.  The
     * bit size should be at least 2048.
     *
     * @param bits size of the key in bits, should be at least 2048
     * @return generated key pair
     */
    public SimpleRSAKeyPair generate(int bits) {
        return SimpleRSAKeyPairImpl.generate(bits);
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
    public SimpleRSAKeyPair generate(int bits, BigInteger exponent) {
        return SimpleRSAKeyPairImpl.generate(bits, exponent);
    }

    /**
     * Reads a path to return a key (private or public) from a
     * PCKS#1 file formatted key (PEM or DER).
     *
     * @param path to the key file (private or public)
     * @return optionally the Key read from the file
     */
    public  <K extends SimpleKey> Attempt<K> read(Path path) {
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
        for(KeyReader reader : this.readerChain) {
            final Attempt<K> keyFound = reader.read(path);
            if(keyFound.successful()) {
                return keyFound;
            }
        }

        return Attempt.fail("No key found in for input file");
    }

    /**
     * Read key from byte array.  Will read PEM or DER format.
     *
     * @param input source byte array
     * @return optionally the Key read from the byte array
     */
    public <K extends SimpleKey> Attempt<K> read(byte[] input) {
        // can't read null or empty bytes
        if(input == null || input.length < 1) {
            return Attempt.fail("Cannot read from empty or null byte array");
        }

        for(KeyReader reader : this.readerChain) {
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
    public <K extends SimpleKey> Attempt<K> read(InputStream inputStream) {
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

        for(KeyReader reader : this.readerChain) {
            final Attempt<K> keyFound = reader.read(inputStream);
            if(keyFound.successful()) {
                return keyFound;
            }
        }

        return Attempt.fail("No key data found in input stream");
    }

    public void write(final SimpleKey key, final Path path) {
        this.write(WriteMode.PEM, key, path);
    }

    public void write(final WriteMode mode, final SimpleKey key, final Path path) {
        switch (mode) {
            case DER:
                RSA.write(key.der(), null, path);
                break;
            case PEM:
            default:
                RSA.write(key.pem(), null, path);
                break;
        }
    }

    public void write(final SimpleKey key, final OutputStream stream) {
        this.write(WriteMode.PEM, key, stream);
    }

    public void write(final WriteMode mode, final SimpleKey key, final OutputStream stream) {
        switch (mode) {
            case DER:
                RSA.write(key.der(), null, stream);
                break;
            case PEM:
            default:
                RSA.write(key.pem(), null, stream);
                break;
        }
    }

    // static write methods:

    private static void write(byte[] bytes, Cipher cipher, Path path) {
        // todo : catch errors with path / files

        // write
        try (final OutputStream stream = Files.newOutputStream(path)) {
            RSA.write(bytes, cipher, stream);
        } catch (IOException e) {
            // todo: handle errors or log
            e.printStackTrace();
        }
    }

    private static void write(byte[] bytes, Cipher cipher, OutputStream stream) {
        // todo: add cipher support

        try(final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            ByteStreams.copy(inputStream, stream);
        } catch (IOException e) {
            // todo: handle errors or log
            e.printStackTrace();
        }
    }
}
