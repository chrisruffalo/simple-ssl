package com.github.chrisruffalo.simplessl.impl.keys.readers;

import com.github.chrisruffalo.simplessl.api.keys.Key;
import com.github.chrisruffalo.simplessl.api.keys.PrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.PublicKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.impl.SimpleReader;
import com.github.chrisruffalo.simplessl.impl.keys.PrivateKeyImpl;
import com.github.chrisruffalo.simplessl.impl.keys.PublicKeyImpl;
import com.github.chrisruffalo.simplessl.impl.keys.rsa.RSAPrivateKeyImpl;
import com.github.chrisruffalo.simplessl.impl.keys.rsa.RSAPublicKeyImpl;

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
public abstract class BaseKeyReader<K extends Key> extends SimpleReader<K> implements KeyReader {

    protected Attempt<PublicKey> wrapPublic(java.security.PublicKey key) {
        PublicKey output = null;
        if(key instanceof RSAPublicKey) {
            output = new RSAPublicKeyImpl((RSAPublicKey)key);
        } else if(key != null) {
            output = new PublicKeyImpl(key);
        }

        if(output == null) {
            return Attempt.fail("Output of reader was null");
        }
        return Attempt.succeed(output);
    }

    protected Attempt<PrivateKey> wrapPrivate(java.security.PrivateKey key) {
        PrivateKey output = null;
        if(key instanceof RSAPrivateKey) {
            output = new RSAPrivateKeyImpl((RSAPrivateKey)key);
        } else if(key != null) {
            output = new PrivateKeyImpl(key);
        }

        if(output == null) {
            return Attempt.fail("Output of reader was null");
        }
        return Attempt.succeed(output);
    }
}
