package org.moera.commons.crypto;

public class Fingerprint {

    private final int version;

    public Fingerprint(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

}
