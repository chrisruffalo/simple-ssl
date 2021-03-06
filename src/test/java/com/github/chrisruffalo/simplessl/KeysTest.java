package com.github.chrisruffalo.simplessl;

import com.google.common.base.Optional;
import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by cruffalo on 2/25/15.
 */
public class KeysTest {

    @Test
    public void test_Read_PrivateRSA_FormatPEM() throws URISyntaxException {
        // get url from class loader
        final URL keyURL = ClassLoader.getSystemClassLoader().getResource("read-test/rsa/pem/private_key.pem");

        // get path from url (through uri)
        final Path path = Paths.get(keyURL.toURI());

        // read
        final Optional<Key> keyOption = Keys.read(path);

        // should be present
        Assert.assertTrue(keyOption.isPresent());

        // get key
        Key key = keyOption.get();
        Assert.assertTrue(key.isPrivate());

        // unwrap
        java.security.Key unwrapped = key.unwrap();
        Assert.assertNotNull(unwrapped);
    }

    @Test
    public void test_Read_PublicRSA_FormatPEM() throws URISyntaxException {
        // get url from class loader
        final URL keyURL = ClassLoader.getSystemClassLoader().getResource("read-test/rsa/pem/public_key.pem");

        // get path from url (through uri)
        final Path path = Paths.get(keyURL.toURI());

        // read
        final Optional<Key> keyOption = Keys.read(path);

        // should be present
        Assert.assertTrue(keyOption.isPresent());

        // get key
        Key key = keyOption.get();
        Assert.assertFalse(key.isPrivate());

        // unwrap
        java.security.Key unwrapped = key.unwrap();
        Assert.assertNotNull(unwrapped);
    }

    @Test
    public void test_Read_PrivateRSAFormatDER() throws URISyntaxException {
        // get url from class loader
        final URL keyURL = ClassLoader.getSystemClassLoader().getResource("read-test/rsa/der/private_key.der");

        // get path from url (through uri)
        final Path path = Paths.get(keyURL.toURI());

        // read
        final Optional<Key> keyOption = Keys.read(path);

        // should be present
        Assert.assertTrue(keyOption.isPresent());

        // get key
        Key key = keyOption.get();
        Assert.assertTrue(key.isPrivate());

        // unwrap
        java.security.Key unwrapped = key.unwrap();
        Assert.assertNotNull(unwrapped);

    }

    @Test
    public void test_Read_PublicRSA_FormatDER() throws URISyntaxException {
        // get url from class loader
        final URL keyURL = ClassLoader.getSystemClassLoader().getResource("read-test/rsa/der/public_key.der");

        // get path from url (through uri)
        final Path path = Paths.get(keyURL.toURI());

        // read
        final Optional<Key> keyOption = Keys.read(path);

        // should be present
        Assert.assertTrue(keyOption.isPresent());

        // get key
        Key key = keyOption.get();
        Assert.assertFalse(key.isPrivate());

        // unwrap
        java.security.Key unwrapped = key.unwrap();
        Assert.assertNotNull(unwrapped);
    }

}
