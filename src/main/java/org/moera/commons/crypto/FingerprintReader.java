package org.moera.commons.crypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class FingerprintReader implements AutoCloseable {

    private final ByteArrayInputStream in;

    FingerprintReader(byte[] input) {
        in = new ByteArrayInputStream(input);
    }

    private Boolean readBoolean() {
        int value = in.read();
        if (value == 0xff) {
            return null;
        } else {
            return value != 0;
        }
    }

    private Long readNumber() {
        int lower = in.read();
        int len;
        switch (lower) {
            case 0xfc:
                len = 2;
                break;
            case 0xfd:
                len = 4;
                break;
            case 0xfe:
                len = 8;
                break;
            case 0xff:
                return null;
            default:
                return (long) lower;
        }
        long value = 0;
        int shift = 0;
        for (int i = 0; i < len; i++) {
            value |= (long) in.read() << shift;
            shift += 8;
        }
        return value;
    }

    private String readString() {
        byte[] bytes = readBytes();
        return bytes != null ? new String(bytes, StandardCharsets.UTF_8) : null;
    }

    private byte[] readBytes() {
        Long len = readNumber();
        if (len == null) {
            return null;
        }
        if (len > in.available()) {
            throw new FingerprintException(byte[].class, "Incorrect length");
        }
        byte[] bytes = new byte[len.intValue()];
        in.read(bytes, 0, bytes.length);
        return bytes;
    }

    private <T> Digest<T> readDigest() {
        byte[] bytes = readBytes();
        if (bytes == null) {
            return null;
        }
        Digest<T> digest = new Digest<>();
        digest.setDigest(bytes);
        return digest;
    }

    private void checkNotNull(Class<?> type, Object value) {
        if (value == null) {
            throw new FingerprintException(type, "null value for primitive type");
        }
    }

    public Object read(Class<?> type) {
        if (type.equals(Boolean.class)) {
            return readBoolean();
        } else if (type.equals(boolean.class)) {
            Boolean bool = readBoolean();
            checkNotNull(type, bool);
            return bool;
        } else if (type.equals(Byte.class)) {
            Long l = readNumber();
            return l != null ? l.byteValue() : null;
        } else if (type.equals(byte.class)) {
            Long l = readNumber();
            checkNotNull(type, l);
            return l.byteValue();
        } else if (type.equals(Short.class)) {
            Long l = readNumber();
            return l != null ? l.shortValue() : null;
        } else if (type.equals(short.class)) {
            Long l = readNumber();
            checkNotNull(type, l);
            return l.shortValue();
        } else if (type.equals(Integer.class)) {
            Long l = readNumber();
            return l != null ? l.intValue() : null;
        } else if (type.equals(int.class)) {
            Long l = readNumber();
            checkNotNull(type, l);
            return l.intValue();
        } else if (type.equals(Long.class)) {
            return readNumber();
        } else if (type.equals(long.class)) {
            Long l = readNumber();
            checkNotNull(type, l);
            return l;
        } else if (type.equals(String.class)) {
            return readString();
        } else if (type.equals(byte[].class)) {
            return readBytes();
        } else if (InetAddress.class.isAssignableFrom(type)) {
            try {
                byte[] bytes = readBytes();
                return bytes != null ? InetAddress.getByAddress(bytes) : null;
            } catch (UnknownHostException e) {
                throw new FingerprintException(type, "incorrect inet address", e);
            }
        } else if (type.equals(Digest.class)) {
            return readDigest();
        } else if (Fingerprint.class.isAssignableFrom(type)) {
            return read(v -> {
                try {
                    return (Fingerprint) type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new FingerprintException(type, "cannot instantiate", e);
                }
            });
        } else {
            throw new FingerprintException(type, "class is not a primitive and not derived from Fingerprint");
        }
    }

    public <T extends Fingerprint> T read(Function<Short, T> creator) {
        Long version = readNumber();
        if (version == null) {
            return null;
        }

        T obj = creator.apply(version.shortValue());
        if (obj == null) {
            throw new CryptoException("cannot create fingerprint");
        }
        if (obj.getVersion() < version) {
            throw new FingerprintException(obj.getClass(), "newer fingerprint version is required");
        }
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                Since since = field.getAnnotation(Since.class);
                if (since == null || since.value() <= obj.getVersion()) {
                    field.set(obj, read(field.getType()));
                }
            }
        } catch (IllegalAccessException e) {
            throw new FingerprintException(obj.getClass(), "cannot read field", e);
        }
        return obj;
    }

    public int available() {
        return in.available();
    }

    @Override
    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            throw new CryptoException(e);
        }
    }

}
