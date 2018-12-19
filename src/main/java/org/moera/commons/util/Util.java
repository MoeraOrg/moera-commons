package org.moera.commons.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;

public class Util {

    public static Timestamp now() {
        return Timestamp.from(Instant.now());
    }

    public static String base64encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] base64decode(String s) {
        return Base64.getDecoder().decode(s);
    }

    public static byte[] toBytes(long l) {
        byte[] bytes = new byte[Long.BYTES];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (l & 0xff);
            l = l >> 8;
        }
        return bytes;
    }

    public static long toLong(byte[] bytes) {
        long l = 0;
        for (byte b : bytes) {
            l = (l << 8) | b;
        }
        return l;
    }

    public static String dump(byte[] bytes) {
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            if (buf.length() > 0) {
                buf.append(' ');
            }
            buf.append(String.format("%02X", b >= 0 ? b : -((int) b)));
        }
        return buf.toString();
    }

}
