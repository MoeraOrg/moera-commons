package org.moera.commons.util;

public class CryptoException extends RuntimeException {

    public CryptoException(String message) {
        super("Crypto exception: " + message);
    }

    public CryptoException(Throwable cause) {
        super("Crypto exception: " + cause.getMessage(), cause);
    }

}
