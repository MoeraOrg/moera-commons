package org.moera.commons.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SignatureDataBuilder {

    private ByteArrayOutputStream out = new ByteArrayOutputStream();

    public SignatureDataBuilder() {
    }

    public void append(char[] cbuf) throws IOException {
        append(cbuf, 0, cbuf.length);
    }

    public void append(char[] cbuf, int off, int len) throws IOException {
        out.write(new String(cbuf, off, len).getBytes(StandardCharsets.UTF_8));
    }

    public void append(String str) throws IOException {
        append(str, 0, str.length());
    }

    public void append(String str, int off, int len) throws IOException {
        if (str != null) {
            out.write(str.substring(off, off + len).getBytes(StandardCharsets.UTF_8));
        }
        out.write(0);
    }

    public void append(byte[] bytes) throws IOException {
        out.write(bytes);
    }

    public void append(long l) throws IOException {
        out.write(Util.toBytes(l));
    }

    public byte[] toBytes() throws IOException {
        out.close();
        return out.toByteArray();
    }

}
