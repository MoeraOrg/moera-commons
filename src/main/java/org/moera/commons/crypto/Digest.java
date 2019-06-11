package org.moera.commons.crypto;

import java.io.IOException;

public class Digest<T> {

    private T value;
    private byte[] digest;

    public Digest() {
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        digest = null;
    }

    public byte[] getDigest() throws IOException {
        if (digest == null) {
            digest = CryptoUtil.digest(value);
        }
        return digest;
    }

}
