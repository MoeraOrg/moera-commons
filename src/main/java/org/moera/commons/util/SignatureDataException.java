package org.moera.commons.util;

public class SignatureDataException extends RuntimeException {

    public SignatureDataException(Class<?> klass) {
        super("Data of unknown class " + klass.getName());
    }

}
