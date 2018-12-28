package org.moera.commons.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Password {

    public static String hash(String password) {
        byte[] salt = new byte[Long.BYTES];
        try {
            SecureRandom.getInstanceStrong().nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
        return hash(password, salt);
    }

    public static boolean validate(String hash, String password) {
        byte[] data = Util.base64decode(hash);
        byte[] salt = new byte[Long.BYTES];
        System.arraycopy(data, data.length - salt.length, salt, 0, salt.length);
        return hash(password, salt).equals(hash);
    }

    private static String hash(String password, byte[] salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
            messageDigest.update(salt);
            byte[] sha = messageDigest.digest();
            byte[] result = new byte[sha.length + salt.length];
            System.arraycopy(sha, 0, result, 0, sha.length);
            System.arraycopy(salt, 0, result, sha.length, salt.length);
            return Util.base64encode(result);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

}
