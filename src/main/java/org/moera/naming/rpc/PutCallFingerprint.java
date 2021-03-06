package org.moera.naming.rpc;

import org.moera.commons.crypto.Fingerprint;

public class PutCallFingerprint extends Fingerprint {

    public String name;
    public int generation;
    public byte[] updatingKey;
    public String nodeUri;
    public byte[] signingKey;
    public long validFrom;
    public byte[] previousDigest;

    public PutCallFingerprint(String name, int generation, byte[] updatingKey, String nodeUri, byte[] signingKey,
                              long validFrom, byte[] previousDigest) {
        super(0);
        this.name = name;
        this.generation = generation;
        this.updatingKey = updatingKey;
        this.nodeUri = nodeUri;
        this.signingKey = signingKey;
        this.validFrom = validFrom;
        this.previousDigest = previousDigest;
    }

}
