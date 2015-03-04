package com.github.chrisruffalo.simplessl.impl.keys;

import com.github.chrisruffalo.simplessl.api.keys.Key;
import com.github.chrisruffalo.simplessl.Keys;
import com.google.common.base.Optional;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
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
public class KeyImplTest {

    private static Path tempDir;

    @BeforeClass
    public static void setupClass() throws IOException {
        KeyImplTest.tempDir = Files.createTempDirectory("simple-ssl-test-");
    }

    @AfterClass
    public static void doneWithClass() {
        try {
            FileUtils.deleteDirectory(KeyImplTest.tempDir.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPemToDer() throws URISyntaxException, IOException {
        // get url from class loader
        final URL keyURL = ClassLoader.getSystemClassLoader().getResource("read-test/rsa/pem/private_key.pem");

        // get path from url (through uri)
        final Path path = Paths.get(keyURL.toURI());

        // read
        final Optional<Key> keyOption = Keys.read(path);

        // found key
        Assert.assertTrue(keyOption.isPresent());

        // create temporary path
        Path tempPemPath = Files.createTempFile(KeyImplTest.tempDir, "private_from_pem_", "_key.pem");
        Path tempDerPath = Files.createTempFile(KeyImplTest.tempDir, "private_from_pem_", "_key.der");

        // write found key to pem path and der path
        final Key key = keyOption.get();

        // write keys
        Keys.writePEM(key, tempPemPath);
        Keys.writeDER(key, tempDerPath);

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
