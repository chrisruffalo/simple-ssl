package com.github.chrisruffalo.simplessl;

import com.github.chrisruffalo.simplessl.impl.io.DERKeyReader;
import com.github.chrisruffalo.simplessl.impl.io.KeyReader;
import com.github.chrisruffalo.simplessl.impl.io.PEMKeyReader;
import com.google.common.base.Optional;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
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

        for(KeyReader reader : Keys.READER_CHAIN) {
            final Optional<K> keyFound = reader.read(path);
            if(keyFound.isPresent()) {
                return keyFound;
            }
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
