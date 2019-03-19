package org.moera.naming.rpc;

import java.io.IOException;

import org.moera.commons.util.SignatureDataBuilder;

public class PutSignatureDataBuilder extends SignatureDataBuilder {

    public PutSignatureDataBuilder(String name, byte[] updatingKey, String nodeUri, long deadline, byte[] signingKey,
                                   long validFrom, byte[] previousDigest) throws IOException {
        append(name);
        append(updatingKey);
        append(nodeUri);
        append(deadline);
        if (previousDigest != null) {
            append(previousDigest);
        } else {
            append((byte) 0, Rules.DIGEST_LENGTH);
        }
        if (signingKey != null) {
            append(signingKey);
            append(validFrom);
        }
    }

}
