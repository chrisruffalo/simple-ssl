package com.github.chrisruffalo.simplessl.impl.keys.readers;

import com.github.chrisruffalo.simplessl.api.keys.SimpleKey;
import com.github.chrisruffalo.simplessl.api.model.Attempt;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Created by cruffalo on 2/26/15.
 */
public interface KeyReader {

    <K extends SimpleKey> Attempt<K> read(final Path path);

    <K extends SimpleKey> Attempt<K> read(final InputStream stream);

    <K extends SimpleKey> Attempt<K> read(final byte[] bytes);

}
