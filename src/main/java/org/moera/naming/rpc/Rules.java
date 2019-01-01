package org.moera.naming.rpc;

import java.time.Duration;
import java.util.regex.Pattern;

public class Rules {

    public static final int NAME_MAX_LENGTH = 127;
    public static final Pattern NAME_PATTERN = Pattern.compile("^[^./:\\s]+$");
    public static final int UPDATING_KEY_MAX_LENGTH = 127;
    public static final int NODE_URI_MAX_LENGTH = 255;
    public static final Duration REGISTRATION_DURATION = Duration.ofDays(365);
    public static final int SIGNING_KEY_MAX_LENGTH = 127;
    public static final Duration VALID_FROM_IN_PAST = Duration.ofDays(7);
    public static final String SIGNATURE_ALGORITHM = "SHA3-256withECDSA";

}
