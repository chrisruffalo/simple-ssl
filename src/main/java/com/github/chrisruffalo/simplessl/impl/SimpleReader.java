package com.github.chrisruffalo.simplessl.impl;

import com.github.chrisruffalo.simplessl.api.model.Attempt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by cruffalo on 5/26/15.
 */
public abstract class SimpleReader<T> {

    public Attempt<T> read(final byte[] bytes) {
        try(final InputStream inputStream = new ByteArrayInputStream(bytes)) {
            return this.read(inputStream);
        } catch (Exception ex) {
            return Attempt.fail("Failed to read key from byte input: " + ex.getMessage(), ex);
        }
    }

    public Attempt<T> read(final Path path) {
        try(final InputStream fileInput = Files.newInputStream(path)) {
            return this.read(fileInput);
        } catch (IOException e) {
            return Attempt.fail("Failed to read key from path: " + e.getMessage(), e);
        }
    }

    public abstract Attempt<T> read(final InputStream inputStream);
}
