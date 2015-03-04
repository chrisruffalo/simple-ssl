package com.github.chrisruffalo.simplessl.impl.io;

import com.github.chrisruffalo.simplessl.api.keys.Key;
import com.google.common.base.Optional;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Created by cruffalo on 2/26/15.
 */
public interface KeyReader {

    <K extends Key> Optional<K> read(Path path);

    <K extends Key> Optional<K> read(InputStream stream);

    <K extends Key> Optional<K> read(byte[] bytes);

}
