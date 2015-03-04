package com.github.chrisruffalo.simplessl.impl.io;

import com.github.chrisruffalo.simplessl.api.keys.Key;
import com.github.chrisruffalo.simplessl.api.keys.PrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.PublicKey;
import com.github.chrisruffalo.simplessl.impl.keys.PrivateKeyImpl;
import com.github.chrisruffalo.simplessl.impl.keys.PublicKeyImpl;
import com.github.chrisruffalo.simplessl.impl.keys.rsa.RSAPrivateKeyImpl;
import com.github.chrisruffalo.simplessl.impl.keys.rsa.RSAPublicKeyImpl;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by cruffalo on 2/26/15.
 */
public abstract class BaseKeyReader implements KeyReader {

    private final Logger logger;

    public BaseKeyReader() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Optional<Key> read(Path path) {
        try(final InputStream fileInput = Files.newInputStream(path)) {
            return this.read(fileInput);
        } catch (IOException e) {
            // todo: log
            return Optional.absent();
        }
    }

    @Override
    public Optional<Key> read(byte[] bytes) {
        try(final InputStream inputStream = new ByteArrayInputStream(bytes)) {
            return this.read(inputStream);
        } catch (Exception ex) {
            // todo: log
            return Optional.absent();
        }
    }

    protected Logger logger() {
        return this.logger;
    }

    protected Optional<PublicKey> wrapPublic(java.security.PublicKey key) {
        PublicKey output = null;
        if(key instanceof RSAPublicKey) {
            output = new RSAPublicKeyImpl((RSAPublicKey)key);
        } else if(key != null) {
            output = new PublicKeyImpl(key);
        }

        if(output == null) {
            return Optional.absent();
        }
        return Optional.of(output);
    }

    protected Optional<PrivateKey> wrapPrivate(java.security.PrivateKey key) {
        PrivateKey output = null;
        if(key instanceof RSAPrivateKey) {
            output = new RSAPrivateKeyImpl((RSAPrivateKey)key);
        } else if(key != null) {
            output = new PrivateKeyImpl(key);
        }

        if(output == null) {
            return Optional.absent();
        }
        return Optional.of(output);
    }
}
