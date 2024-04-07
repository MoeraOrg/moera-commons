package org.moera.commons.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.moera.commons.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FingerprintWriter implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(FingerprintWriter.class);

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    FingerprintWriter() {
    }

    private void appendNull() {
        log.trace("value: null");
        out.write((byte) 0xff);
    }

    private void append(String str) {
        if (str == null) {
            appendNull();
            return;
        }
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        if (log.isTraceEnabled()) {
            log.trace("value: {}", LogUtil.format(bytes));
        }
        append(bytes.length);
        try {
            out.write(bytes);
        } catch (IOException e) {
            throw new CryptoException(e);
        }
    }

    private void append(boolean b) {
        log.trace("value: {}", LogUtil.format(b));
        out.write((byte) (b ? 1 : 0));
    }

    private void append(long l) {
        log.trace("value: {}", LogUtil.format(l));
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
        if (log.isTraceEnabled()) {
            log.trace("value: {}", LogUtil.format(bytes));
        }
        try {
            out.write(bytes);
        } catch (IOException e) {
            throw new CryptoException(e);
        }
    }

    private void append(List<?> objects) {
        if (log.isTraceEnabled()) {
            log.trace("list: {}", LogUtil.format(objects.size()));
        }
        try (FingerprintWriter writer = new FingerprintWriter()) {
            objects.forEach(writer::append);
            append(writer.toBytes());
        }
    }

    private void appendFingerprint(Fingerprint obj) {
        log.trace("Fingerprinting {} (ver {})", obj.getClass().getName(), obj.getVersion());
        append(obj.getVersion());
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                Since since = field.getAnnotation(Since.class);
                if (since == null || since.value() <= obj.getVersion()) {
                    log.trace("field: {}", field.getName());
                    append(field.get(obj));
                }
            }
        } catch (IllegalAccessException e) {
            throw new FingerprintException(obj.getClass(), "cannot read field", e);
        } finally {
            log.trace("End of fingerprint {}", obj.getClass().getName());
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
            append((String) obj);
        } else if (obj instanceof byte[]) {
            append((byte[]) obj);
        } else if (obj instanceof InetAddress) {
            append(((InetAddress) obj).getAddress());
        } else if (obj instanceof Digest) {
            append(((Digest<?>) obj).getDigest());
        } else if (obj instanceof List) {
            append((List<?>) obj);
        } else if (obj instanceof Fingerprint) {
            appendFingerprint((Fingerprint) obj);
        } else {
            throw new FingerprintException(obj.getClass(), "class is not a primitive and not derived from Fingerprint");
        }
    }

    @Override
    public void close() {
        try {
            out.close();
        } catch (IOException e) {
            throw new CryptoException(e);
        }
    }

    public byte[] toBytes() {
        return out.toByteArray();
    }

}
