package org.moera.commons.util;

import java.util.UUID;

public class LogUtil {

    public static String format(String value) {
        return value != null ? String.format("'%s'", escape(value)) : "null";
    }

    public static String format(Integer value) {
        return value != null ? Integer.toString(value) : "null";
    }

    public static String format(Long value) {
        return value != null ? Long.toString(value) : "null";
    }

    private static String escape(String value) {
        return value.replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    public static String format(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return format(value);
        } else {
            return format(value.substring(0, maxLength) + "...");
        }
    }

    public static String format(UUID value) {
        return value != null ? String.format("'%s'", value.toString()) : "null";
    }

    public static String format(byte[] value) {
        return value != null ? Util.dump(value) : "null";
    }

    public static String formatTimestamp(Long value) {
        return value != null ? Util.formatTimestamp(value) : "null";
    }

}
