package org.moera.naming.rpc;

import java.util.Arrays;

public enum OperationStatus {

    WAITING,
    ADDED,
    STARTED,
    SUCCEEDED,
    FAILED,
    UNKNOWN;

    public String getValue() {
        return name().toLowerCase();
    }

    public static OperationStatus forValue(String value) {
        if (value == null) {
            return null;
        }
        return Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(value)).findFirst().orElse(null);
    }

}
