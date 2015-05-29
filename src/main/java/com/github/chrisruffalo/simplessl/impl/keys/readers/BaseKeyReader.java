package com.github.chrisruffalo.simplessl.impl.keys.readers;

import com.github.chrisruffalo.simplessl.api.keys.SimpleKey;
import com.github.chrisruffalo.simplessl.api.keys.SimplePrivateKey;
import com.github.chrisruffalo.simplessl.api.keys.SimplePublicKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.impl.SimpleReader;
import com.github.chrisruffalo.simplessl.impl.keys.SimplePrivateKeyImpl;
import com.github.chrisruffalo.simplessl.impl.keys.SimplePublicKeyImpl;
import com.github.chrisruffalo.simplessl.impl.keys.rsa.SimpleRSAPrivateKeyImpl;
import com.github.chrisruffalo.simplessl.impl.keys.rsa.SimpleRSAPublicKeyImpl;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by cruffalo on 2/26/15.
 */
public abstract class BaseKeyReader extends SimpleReader<SimpleKey> implements KeyReader {

    protected Attempt<SimplePublicKey> wrapPublic(java.security.PublicKey key) {
        SimplePublicKey output = null;
        if(key instanceof RSAPublicKey) {
            output = new SimpleRSAPublicKeyImpl((RSAPublicKey)key);
        } else if(key != null) {
            output = new SimplePublicKeyImpl(key);
        }

        if(output == null) {
            return Attempt.fail("Output of reader was null");
        }
        return Attempt.succeed(output);
    }

    protected Attempt<SimplePrivateKey> wrapPrivate(java.security.PrivateKey key) {
        SimplePrivateKey output = null;
        if(key instanceof RSAPrivateKey) {
            output = new SimpleRSAPrivateKeyImpl((RSAPrivateKey)key);
        } else if(key != null) {
            output = new SimplePrivateKeyImpl(key);
        }

        if(output == null) {
            return Attempt.fail("Output of reader was null");
        }
        return Attempt.succeed(output);
    }
}
