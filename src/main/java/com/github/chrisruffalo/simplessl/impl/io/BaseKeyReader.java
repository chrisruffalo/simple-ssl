package com.github.chrisruffalo.simplessl.impl.io;

import com.github.chrisruffalo.simplessl.Key;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by cruffalo on 2/26/15.
 */
public abstract class BaseKeyReader implements  KeyReader {

    private final Logger logger;

    public BaseKeyReader() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public <K extends Key> Optional<K> read(Path path) {
        try(final InputStream fileInput = Files.newInputStream(path)) {
            return this.read(fileInput);
        } catch (IOException e) {
            // todo: log
            return Optional.absent();
        }
    }

    @Override
    public <K extends Key> Optional<K> read(byte[] bytes) {
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

}
