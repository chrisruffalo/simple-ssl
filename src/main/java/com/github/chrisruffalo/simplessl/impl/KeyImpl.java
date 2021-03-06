package com.github.chrisruffalo.simplessl.impl;

import com.github.chrisruffalo.simplessl.Key;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by cruffalo on 2/25/15.
 */
public abstract class KeyImpl implements Key {

    @Override
    public byte[] der() {

        // get pem bytes
        final byte[] bytes = this.pem();

        // ensure total is ok
        final String total = new String(bytes);
        if(total == null || total.trim().isEmpty()) {
            return new byte[0];
        }

        // split input message with a little bit in there for cross-platform
        // compatibility
        final String[] lines = total.split("\\r?\\n");
        if(lines.length < 3) {
            return new byte[0];
        }

        // remove the first line and the last line of the message
        final StringBuilder after = new StringBuilder();
        for(int i = 1; i < lines.length-1; i++) {
            after.append(lines[i]);
        }

        // decode
        byte[] output = Base64.decode(after.toString().getBytes());

        // return output
        return output;
    }

    @Override
    public byte[] pem() {
        // create byte array stream and wrap it in a writer
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer destination = new OutputStreamWriter(baos);

        // write (unwrapped object)
        try(final JcaPEMWriter pemWriter = new JcaPEMWriter(destination)) {
            pemWriter.writeObject(this.unwrap());
        } catch (IOException e) {
            // todo: log error

            // return 0 bytes
            return new byte[0];
        }

        final byte[] bytes = baos.toByteArray();

        return bytes;
    }

    @Override
    public String getAlgorithm() {
        return this.unwrap().getAlgorithm();
    }

    @Override
    public String getFormat() {
        return this.unwrap().getFormat();
    }

    @Override
    public byte[] getEncoded() {
        return this.unwrap().getEncoded();
    }
}
