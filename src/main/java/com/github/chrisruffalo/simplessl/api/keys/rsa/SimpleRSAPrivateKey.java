package com.github.chrisruffalo.simplessl.api.keys.rsa;

import com.github.chrisruffalo.simplessl.api.keys.SimpleKey;
import com.github.chrisruffalo.simplessl.api.keys.SimplePrivateKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;

/**
 * Created by cruffalo on 3/3/15.
 */
public interface SimpleRSAPrivateKey extends SimpleRSAKey, SimplePrivateKey<SimpleRSAPublicKey>, SimpleKey, java.security.interfaces.RSAPrivateCrtKey {

    Attempt<SimpleRSAPublicKey> publicKey();

}
