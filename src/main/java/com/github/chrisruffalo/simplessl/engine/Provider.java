package com.github.chrisruffalo.simplessl.engine;

import com.github.chrisruffalo.simplessl.api.SupportedCipherType;
import com.github.chrisruffalo.simplessl.api.SupportedKeyPairType;
import com.github.chrisruffalo.simplessl.api.SupportedKeyType;
import com.github.chrisruffalo.simplessl.api.SupportedSignatureType;
import org.bouncycastle.crypto.params.DSAKeyGenerationParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.DSAParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

/**
 * Created by cruffalo on 2/24/15.
 */
public final class Provider {

    // BouncyCastle provider ID
    public static final String PROVIDER_ID = "BC";

    static {
        if(null == Security.getProvider(Provider.PROVIDER_ID)) {
            // create provider
            final java.security.Provider provider = new BouncyCastleProvider();
            // configure provider

            // add provider to available providers
            Security.addProvider(provider);
        }
    }

    public static KeyPairGenerator getRSAKeyPairGenerator(int bits, BigInteger exponent) {

        try {
            // generate key pair generator
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(SupportedKeyPairType.RSA.toString(), Provider.PROVIDER_ID);

            // set up spec
            final RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(bits, exponent);

            // initialize
            keyPairGenerator.initialize(spec);

            // return
            return keyPairGenerator;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not find algorithm '" + SupportedKeyPairType.RSA.toString() + "', ensure BouncyCastle libraries are available", e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("Could not find provider '" + Provider.PROVIDER_ID + "', ensure BouncyCastle libraries are available", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Internal error: could not configure algorithm ('" + e.getMessage() + "')", e);
        }
    }

    public static KeyPairGenerator getDSAKeyPairGenerator(BigInteger p, BigInteger q, BigInteger g) {

        try {
            // generate key pair generator
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(SupportedKeyPairType.DSA.toString(), Provider.PROVIDER_ID);

            // set up spec
            final DSAParameterSpec spec = new DSAParameterSpec(p,q,g);

            // initialize
            keyPairGenerator.initialize(spec);

            // return
            return keyPairGenerator;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not find algorithm '" + SupportedKeyPairType.RSA.toString() + "', ensure BouncyCastle libraries are available", e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("Could not find provider '" + Provider.PROVIDER_ID + "', ensure BouncyCastle libraries are available", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Internal error: could not configure algorithm ('" + e.getMessage() + "')", e);
        }
    }

    public static KeyFactory getKeyFactory(SupportedKeyType type) {
        try {
            // create key factory
            final KeyFactory keyFactory = KeyFactory.getInstance(type.toString(), Provider.PROVIDER_ID);

            // return for use
            return keyFactory;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not find algorithm '" + type.toString() + "', ensure BouncyCastle libraries are available", e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("Could not find provider '" + Provider.PROVIDER_ID + "', ensure BouncyCastle libraries are available", e);
        }
    }

    public static Signature getSignature(SupportedSignatureType signatureType) {
        try {
            // lookup signature
            final Signature signature = Signature.getInstance(signatureType.toString(), Provider.PROVIDER_ID);

            // return for use
            return signature;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not find algorithm: " + signatureType.toString() + " with error: " + e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("Could not find provider '" + Provider.PROVIDER_ID + "', ensure BouncyCastle libraries are available", e);
        }

    }

    public static Cipher getCipher(SupportedCipherType type) {

        try {
            // create cipher
            final Cipher cipher = Cipher.getInstance(type.toString(), Provider.PROVIDER_ID);

            // return cipher
            return cipher;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not find cipher from description '" + type.toString() + "', ensure BouncyCastle libraries are available", e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("Could not find provider '" + Provider.PROVIDER_ID + "', ensure BouncyCastle libraries are available", e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("Invalid padding with message:'" + e.getMessage() + "', see BouncyCastle documentation for supported padding types", e);
        }
    }

    public static ContentSigner getContentSigner(SupportedSignatureType type, PrivateKey privateKey) {
        // build provider
        final JcaContentSignerBuilder builder = new JcaContentSignerBuilder(type.toString());

        // return content signer from provider
        try {
            return builder.setProvider(Provider.PROVIDER_ID).build(privateKey);
        } catch (OperatorCreationException e) {
            throw new RuntimeException("Could not build content signer from provided settings with error: " + e.getMessage());
        }
    }

}
