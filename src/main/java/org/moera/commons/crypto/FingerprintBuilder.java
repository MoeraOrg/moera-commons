package org.moera.commons.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

class FingerprintBuilder {

    private ByteArrayOutputStream out = new ByteArrayOutputStream();

    FingerprintBuilder() {
    }

    private void appendNull() {
        out.write((byte) 0xff);
    }

    private void append(String str, int off, int len) {
        if (str == null) {
            appendNull();
            return;
        }
        byte[] bytes = str.substring(off, off + len).getBytes(StandardCharsets.UTF_8);
        append(bytes.length);
        try {
            out.write(bytes);
        } catch (IOException e) {
            throw new CryptoException(e);
        }
    }

    private void append(boolean b) {
        out.write((byte) (b ? 1 : 0));
    }

    private void append(long l) {
        int len;
        if (l < 0xfc) {
            len = 1;
        } else if (l <= 0xffff) {
            out.write((byte) 0xfc);
            len = 2;
        } else if (l <= 0xffffffffL) {
            out.write((byte) 0xfd);
            len = 4;
        } else {
            out.write((byte) 0xfe);
            len = 8;
        }
        for (int i = 0; i < len; i++) {
            out.write((byte) (l & 0xff));
            l = l >> 8;
        }
    }

    private void append(byte[] bytes) {
        append(bytes.length);
        try {
            out.write(bytes);
        } catch (IOException e) {
            throw new CryptoException(e);
        }
    }

    private void appendFingerprint(Fingerprint obj) {
        append(obj.getVersion());
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                Since since = field.getAnnotation(Since.class);
                if (since == null || since.value() <= obj.getVersion()) {
                    append(field.get(obj));
                }
            }
        } catch (IllegalAccessException e) {
            throw new FingerprintException(obj.getClass(), "cannot read field", e);
        }
    }

    public void append(Object obj) {
        if (obj == null) {
            appendNull();
            return;
        }
        if (obj instanceof Boolean) {
            append(((Boolean) obj).booleanValue());
        } else if (obj instanceof Byte) {
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
            append((byte[]) obj);
        } else if (obj instanceof Digest) {
            append(((Digest) obj).getDigest());
        } else if (obj instanceof Fingerprint) {
            appendFingerprint((Fingerprint) obj);
        } else {
            throw new FingerprintException(obj.getClass(), "class is not a primitive and not derived from Fingerprint");
        }
    }

    public byte[] toBytes() {
        try {
            out.close();
        } catch (IOException e) {
            throw new CryptoException(e);
        }
        return out.toByteArray();
    }

}
