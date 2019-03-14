package org.moera.commons.util;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class Util {

    public static Timestamp now() {
        return Timestamp.from(Instant.now());
    }

    public static String formatTimestamp(long timestamp) {
        return DateTimeFormatter.ISO_DATE_TIME.format(Instant.ofEpochSecond(timestamp));
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

    public static Duration toDuration(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        ChronoUnit unit;
        switch (s.charAt(s.length() - 1)) {
            case 's':
                unit = ChronoUnit.SECONDS;
                break;
            case 'm':
                unit = ChronoUnit.MINUTES;
                break;
            case 'h':
                unit = ChronoUnit.HOURS;
                break;
            case 'd':
                unit = ChronoUnit.DAYS;
                break;
            default:
                throw new DurationFormatException(s);
        }
        long amount;
        try {
            amount = Long.parseLong(s.substring(0, s.length() - 1));
        } catch (NumberFormatException e) {
            throw new DurationFormatException(s);
        }
        return Duration.of(amount, unit);
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

    public static boolean equals(byte[] array1, int offset1, byte[] array2, int offset2, int length) {
        if (offset1 < 0 || offset2 < 0 || offset1 + length > array1.length || offset2 + length > array2.length) {
            throw new IllegalArgumentException("Array indexes out of range");
        }
        for (int i = 0; i < length; i++) {
            if (array1[offset1 + i] != array2[offset2 + i]) {
                return false;
            }
        }
        return true;
    }

}
