package com.github.chrisruffalo.simplessl.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by cruffalo on 5/20/15.
 */
public final class TempUtil {

    public static final String TEMP_DIR_ROOT = System.getProperty("java.io.tmpdir", "/tmp");

    public static final AtomicReference<Path> tmpPath = new AtomicReference<>(null);

    private TempUtil() {

    }

    public synchronized static Path get() {
        if(TempUtil.tmpPath.get() != null) {
            return TempUtil.tmpPath.get();
        }

        // check temp root
        final Path tmpRoot = Paths.get(TempUtil.TEMP_DIR_ROOT);
        if(Files.exists(tmpRoot)) {
            try {
                Files.createDirectories(tmpRoot);
            } catch (IOException e) {
                throw new RuntimeException("Could not create missing temprory directory path: " + tmpRoot.toString());
            }
        }

        // create temp dir and return
        try {
            // create dir and save for shared dir
            final Path dir = Files.createTempDirectory(tmpRoot, ".simplessl-test-");
            TempUtil.tmpPath.set(dir);

            // set up delete-on-exit
            /*
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    try {
                        Iterator<Path> filesIt = Files.newDirectoryStream(dir).iterator();
                        while(filesIt.hasNext()) {
                            final Path current = filesIt.next();
                            Files.deleteIfExists(current);
                        }
                        Files.deleteIfExists(dir);
                    } catch (IOException e) {
                        // couldn't delete, oh well
                    }
                }
            });
            */

            return dir;
        } catch (IOException e) {
            throw new RuntimeException("Could not create temporary directory for test inside: " + tmpRoot.toString());
        }
    }

}
