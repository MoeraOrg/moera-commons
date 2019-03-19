package org.moera.naming.rpc;

import java.time.Duration;
import java.util.regex.Pattern;

public class Rules {

    public static final int NAME_MAX_LENGTH = 127;
    public static final Pattern NAME_PATTERN = Pattern.compile("^[^./:\\s]+$");
    public static final String EC_CURVE = "secp256k1";
    public static final int PRIVATE_KEY_LENGTH = 32;
    public static final int ENCODED_PRIVATE_KEY_LENGTH = 44;
    public static final int PUBLIC_KEY_LENGTH = 64;
    public static final int ENCODED_PUBLIC_KEY_LENGTH = 88;
    public static final int NODE_URI_MAX_LENGTH = 255;
    public static final Duration REGISTRATION_DURATION = Duration.ofDays(365);
    public static final Duration VALID_FROM_IN_PAST = Duration.ofDays(7);
    public static final String DIGEST_ALGORITHM = "SHA3-256";
    public static final int DIGEST_LENGTH = 32;
    public static final String SIGNATURE_ALGORITHM = "SHA3-256withECDSA";
    public static final int SIGNATURE_MAX_LENGTH = 72; // FIXME must be exact value

}
