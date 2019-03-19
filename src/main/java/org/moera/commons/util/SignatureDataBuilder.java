package org.moera.commons.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.jcajce.provider.util.DigestFactory;
import org.moera.naming.rpc.Rules;

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

    public void append(byte b, int n) throws IOException {
        for (int i = 0; i < n; i++) {
            out.write(b);
        }
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

    public byte[] getDigest() throws IOException {
        Digest digest = DigestFactory.getDigest(Rules.DIGEST_ALGORITHM);
        byte[] content = toBytes();
        digest.update(content, 0, content.length);
        byte[] result = new byte[digest.getDigestSize()];
        digest.doFinal(result, 0);
        return result;
    }

}
