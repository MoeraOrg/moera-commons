package org.moera.commons.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class Util {

    public static Long toEpochSecond(Timestamp timestamp) {
        return timestamp != null ? timestamp.getTime() / 1000 : null;
    }

    public static String formatTimestamp(long timestamp) {
        return DateTimeFormatter.ISO_DATE_TIME.format(
                LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC));
    }

    public static String base64encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] base64decode(String s) {
        return Base64.getDecoder().decode(s);
    }

    public static String base64urlencode(byte[] bytes) {
        return bytes != null ? Base64.getUrlEncoder().withoutPadding().encodeToString(bytes) : null;
    }

    public static String dump(byte[] bytes) {
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            if (!buf.isEmpty()) {
                buf.append(' ');
            }
            buf.append(hexByte(b));
        }
        return buf.toString();
    }

    public static String dumpShort(byte[] bytes) {
        if (bytes.length <= 16) {
            return dump(bytes);
        }

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (!buf.isEmpty()) {
                buf.append(' ');
            }
            buf.append(hexByte(bytes[i]));
        }
        buf.append(" ...");
        for (int i = bytes.length - 8; i < bytes.length; i++) {
            buf.append(' ');
            buf.append(hexByte(bytes[i]));
        }
        return buf.toString();
    }

    public static String hexByte(byte b) {
        return String.format("%02X", b >= 0 ? b : 256 + (int) b);
    }

}
