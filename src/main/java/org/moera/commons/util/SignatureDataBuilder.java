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

    public void appendNull() {
        out.write((byte) 0xff);
    }

    public void append(String str, int off, int len) throws IOException {
        if (str == null) {
            appendNull();
            return;
        }
        byte[] bytes = str.substring(off, off + len).getBytes(StandardCharsets.UTF_8);
        append(bytes.length);
        out.write(bytes);
    }

    public void append(long l) {
        int len;
        if (l < 0xfc) {
            len = 1;
        } else if (l <= 0xffff) {
            out.write((byte) 0xfc);
            len = 2;
        } else if (l <= 0xffffff) {
            out.write((byte) 0xfd);
            len = 3;
        } else {
            out.write((byte) 0xfe);
            len = 4;
        }
        for (int i = 0; i < len; i++) {
            out.write((byte) (l & 0xff));
            l = l >> 8;
        }
    }

    public void append(Object obj) throws IOException {
        if (obj == null) {
            appendNull();
            return;
        }
        if (obj instanceof Byte) {
            append(((Byte) obj).longValue());
        } else if (obj instanceof Short) {
            append(((Short) obj).longValue());
        } else if (obj instanceof Integer) {
            append(((Integer) obj).longValue());
        } else if (obj instanceof Long) {
            append(((Long) obj).longValue());
        } else if (obj instanceof String) {
            String str = (String) obj;
            append(str, 0, str.length());
        } else if (obj instanceof byte[]) {
            byte[] bytes = (byte[]) obj;
            append(bytes.length);
            out.write(bytes);
        } else {
            throw new SignatureDataException(obj.getClass());
        }
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
