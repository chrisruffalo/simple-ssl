package com.github.chrisruffalo.simplessl.impl.keys;

import com.github.chrisruffalo.simplessl.SimpleSSL;
import com.github.chrisruffalo.simplessl.api.WriteMode;
import com.github.chrisruffalo.simplessl.api.keys.SimpleKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.util.TempUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by cruffalo on 2/25/15.
 */
public class SimpleKeyImplTest {

    private static final Path TEMP_DIR = TempUtil.get();

    @Test
    public void testPemToDer() throws URISyntaxException, IOException {
        // get url from class loader
        final URL keyURL = ClassLoader.getSystemClassLoader().getResource("read-test/rsa/pem/private_key.pem");

        // get path from url (through uri)
        final Path path = Paths.get(keyURL.toURI());

        // read
        final Attempt<SimpleKey> keyOption = SimpleSSL.RSA().read(path);

        // found key
        Assert.assertTrue(keyOption.successful());

        // create temporary path
        Path tempPemPath = Files.createTempFile(SimpleKeyImplTest.TEMP_DIR, "private_from_pem_", "_key.pem");
        Path tempDerPath = Files.createTempFile(SimpleKeyImplTest.TEMP_DIR, "private_from_pem_", "_key.der");

        // write found key to pem path and der path
        final SimpleKey key = keyOption.get();

        // make sure data is present
        final byte[] der = key.der();
        final byte[] pem = key.pem();

        Assert.assertTrue(der.length > 0);
        Assert.assertTrue(pem.length > 0);

        // write keys
        SimpleSSL.RSA().write(key, tempPemPath);
        SimpleSSL.RSA().write(WriteMode.DER, key, tempDerPath);

        // now we want to verify that the der key is the same as the der key found in read-test/rsa/der/private_key.der
        // and that the output pem key is the same as the one read in for this test
        Assert.assertTrue(FileUtils.contentEquals(path.toFile(), tempPemPath.toFile()));

        // get url from class loader
        final URL derKeyURL = ClassLoader.getSystemClassLoader().getResource("read-test/rsa/der/private_key.der");

        // get path from url (through uri)
        final Path derPath = Paths.get(derKeyURL.toURI());

        // assert der is the same as the file converted by openssl
        Assert.assertTrue(FileUtils.contentEquals(derPath.toFile(), tempDerPath.toFile()));
    }
}
