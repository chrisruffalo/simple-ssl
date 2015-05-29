package com.github.chrisruffalo.simplessl;

import com.github.chrisruffalo.simplessl.commands.RSA;
import com.github.chrisruffalo.simplessl.commands.X509;

/**
 * <p>
 *  Simple entry point for the SimpleSSL toolkit.  This matches the
 *  structure used by the OpenSSL command. This structure is generally
 *  'openssl &gt;command&lt;' where the `command` is something like "x509"
 *  or "rsa".
 * </p>
 * <p>
 *  So the purpose of this tool is to provide that same familiar endpoint
 *  and organize the same way to allow end users to quickly orient themselves
 *  with the use of the toolkit.
 * </p>
 */
public final class SimpleSSL {

    private static final X509 X509 = new X509();
    private static final RSA RSA = new RSA();

    // empty static block
    static {

    }

    // can't construct
    private SimpleSSL() {

    }

    public static RSA RSA() {
        return SimpleSSL.RSA;
    }

    public static X509 X509() {
        return SimpleSSL.X509;
    }
}
