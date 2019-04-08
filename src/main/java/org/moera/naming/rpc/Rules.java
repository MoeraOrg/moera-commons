package org.moera.naming.rpc;

import java.time.Duration;
import java.util.regex.Pattern;

public class Rules {

    public static final int NAME_MAX_LENGTH = 127;
    public static final String NAME_PUNCTUATION_ALLOWED ="!%&*-.?";
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

    private static final Pattern LATIN_CHARS = Pattern.compile("^[A-Za-z]+$");

    public static boolean isNameValid(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (name.length() <= 3 && LATIN_CHARS.matcher(name).matches()) {
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            if (!isNameCharacterValid(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNameCharacterValid(char c) {
        switch (Character.getType(c)) {
            case Character.UPPERCASE_LETTER:
            case Character.LOWERCASE_LETTER:
            case Character.TITLECASE_LETTER:
            case Character.OTHER_LETTER:
            case Character.DECIMAL_DIGIT_NUMBER:
            case Character.LETTER_NUMBER:
            case Character.OTHER_NUMBER:
            case Character.CURRENCY_SYMBOL:
            case Character.OTHER_SYMBOL:
                return true;

            case Character.MATH_SYMBOL:
                return c != '~';

            case Character.OTHER_PUNCTUATION:
            case Character.DASH_PUNCTUATION:
                return NAME_PUNCTUATION_ALLOWED.indexOf(c) >= 0;

            default:
                return false;
        }
    }

}
