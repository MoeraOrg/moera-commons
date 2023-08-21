package org.moera.naming.rpc;

import java.time.Duration;
import java.util.regex.Pattern;

public class Rules {

    public static final int NAME_MAX_LENGTH = 127;
    public static final String NAME_PUNCTUATION_ALLOWED ="!*-.";
    public static final String EC_CURVE = "secp256k1";
    public static final int PRIVATE_KEY_LENGTH = 32;
    public static final int PUBLIC_KEY_LENGTH = 64;
    public static final int NODE_URI_MAX_LENGTH = 255;
    public static final Duration VALID_FROM_IN_PAST = Duration.ofDays(7);
    public static final String DIGEST_ALGORITHM = "SHA3-256";
    public static final int DIGEST_LENGTH = 32;
    public static final String SIGNATURE_ALGORITHM = "SHA3-256withECDSA";
    public static final int SIGNATURE_MAX_LENGTH = 72; // FIXME must be exact value
    public static final int PAGE_MAX_SIZE = 100;

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
        return switch (Character.getType(c)) {
            case Character.UPPERCASE_LETTER,
                    Character.LOWERCASE_LETTER,
                    Character.TITLECASE_LETTER,
                    Character.OTHER_LETTER,
                    Character.DECIMAL_DIGIT_NUMBER,
                    Character.LETTER_NUMBER,
                    Character.OTHER_NUMBER,
                    Character.CURRENCY_SYMBOL,
                    Character.OTHER_SYMBOL -> true;
            case Character.OTHER_PUNCTUATION,
                    Character.DASH_PUNCTUATION -> NAME_PUNCTUATION_ALLOWED.indexOf(c) >= 0;
            default -> false;
        };
    }

}
