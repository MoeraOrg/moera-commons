package org.moera.commons.util;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class CryptoUtil {

    private static final byte[] X509_HEADER = Util.base64decode("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE");
    private static final byte[] PKCS8_HEADER = Util.base64decode("MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCA=");

    public static byte[] toRawPublicKey(PublicKey publicKey) {
        byte[] encodedKey = publicKey.getEncoded();
        byte[] rawKey = new byte[encodedKey.length - X509_HEADER.length];
        System.arraycopy(encodedKey, X509_HEADER.length, rawKey, 0, rawKey.length);
        return rawKey;
    }

    public static PublicKey toPublicKey(byte[] rawKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedKey = new byte[X509_HEADER.length + rawKey.length];
        System.arraycopy(X509_HEADER, 0, encodedKey, 0, X509_HEADER.length);
        System.arraycopy(rawKey, 0, encodedKey, X509_HEADER.length, rawKey.length);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        return KeyFactory.getInstance("EC").generatePublic(keySpec);
    }

    public static byte[] toRawPrivateKey(PrivateKey privateKey) {
        byte[] encodedKey = privateKey.getEncoded();
        byte[] rawKey = new byte[encodedKey.length - PKCS8_HEADER.length];
        System.arraycopy(encodedKey, PKCS8_HEADER.length, rawKey, 0, rawKey.length);
        return rawKey;
    }

    public static PrivateKey toPrivateKey(byte[] rawKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedKey = new byte[PKCS8_HEADER.length + rawKey.length];
        System.arraycopy(PKCS8_HEADER, 0, encodedKey, 0, PKCS8_HEADER.length);
        System.arraycopy(rawKey, 0, encodedKey, PKCS8_HEADER.length, rawKey.length);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
        return KeyFactory.getInstance("EC").generatePrivate(keySpec);
    }

}
