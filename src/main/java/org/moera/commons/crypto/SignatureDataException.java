package org.moera.commons.crypto;

public class SignatureDataException extends RuntimeException {

    public SignatureDataException(Class<?> klass, String reason) {
        super(getMessage(klass, reason));
    }

    public SignatureDataException(Class<?> klass, String reason, Throwable cause) {
        super(getMessage(klass, reason), cause);
    }

    private static String getMessage(Class<?> klass, String reason) {
        return String.format("Cannot fingerprint class %s: %s", klass.getName(), reason);
    }

}
