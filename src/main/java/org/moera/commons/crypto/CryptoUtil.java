package org.moera.commons.crypto;

import static org.moera.naming.rpc.Rules.EC_CURVE;
import static org.moera.naming.rpc.Rules.PRIVATE_KEY_LENGTH;
import static org.moera.naming.rpc.Rules.PUBLIC_KEY_LENGTH;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.jcajce.provider.util.DigestFactory;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.moera.commons.util.Util;
import org.moera.naming.rpc.Rules;

public class CryptoUtil {

    private static byte[] encodeUnsigned(BigInteger v, int len) {
        byte[] r = v.toByteArray();
        byte[] e = new byte[len];
        int srcPos = r.length < len ? 0 : r.length - len;
        int dstPos = r.length < len ? len - r.length : 0;
        System.arraycopy(r, srcPos, e, dstPos, r.length - srcPos);
        return e;
    }

    private static BigInteger decodeUnsigned(byte[] e) {
        byte[] r = new byte[e.length + 1];
        System.arraycopy(e, 0, r, r.length - e.length, e.length);
        return new BigInteger(r);
    }

    public static byte[] toRawPublicKey(ECPublicKey publicKey) {
        byte[] x = encodeUnsigned(publicKey.getW().getAffineX(), PUBLIC_KEY_LENGTH / 2);
        byte[] y = encodeUnsigned(publicKey.getW().getAffineY(), PUBLIC_KEY_LENGTH / 2);
        byte[] rawKey = new byte[x.length + y.length];
        System.arraycopy(x, 0, rawKey, 0, x.length);
        System.arraycopy(y, 0, rawKey, x.length, y.length);
        return rawKey;
    }

    public static ECPublicKey toPublicKey(byte[] rawKey) {
        byte[] x = new byte[PUBLIC_KEY_LENGTH / 2];
        byte[] y = new byte[PUBLIC_KEY_LENGTH / 2];
        System.arraycopy(rawKey, 0, x, 0, x.length);
        System.arraycopy(rawKey, x.length, y, 0, y.length);
        ECParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec(EC_CURVE);
        ECPoint w = parameterSpec.getCurve().createPoint(decodeUnsigned(x), decodeUnsigned(y));
        ECPublicKeySpec keySpec = new ECPublicKeySpec(w, parameterSpec);

        try {
            return (ECPublicKey) KeyFactory.getInstance("EC", "BC").generatePublic(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new CryptoException(e);
        }
    }

    public static byte[] toRawPrivateKey(ECPrivateKey privateKey) {
        return encodeUnsigned(privateKey.getS(), PRIVATE_KEY_LENGTH);
    }

    public static ECPrivateKey toPrivateKey(byte[] rawKey) {
        ECParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec(EC_CURVE);
        ECPrivateKeySpec keySpec = new ECPrivateKeySpec(decodeUnsigned(rawKey), parameterSpec);
        try {
            return (ECPrivateKey) KeyFactory.getInstance("EC", "BC").generatePrivate(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new CryptoException(e);
        }
    }

    public static String token() {
        byte[] random = new byte[32];
        try {
            SecureRandom.getInstanceStrong().nextBytes(random);
            return Util.base64encode(MessageDigest.getInstance("SHA-256", "BC").digest(random));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new CryptoException(e);
        }
    }

    public static byte[] fingerprint(Object obj) throws IOException {
        FingerprintBuilder buf = new FingerprintBuilder();
        buf.append(obj);
        return buf.toBytes();
    }

    public static byte[] digest(Object obj) throws IOException {
        byte[] content = fingerprint(obj);
        Digest digest = DigestFactory.getDigest(Rules.DIGEST_ALGORITHM);
        digest.update(content, 0, content.length);
        byte[] result = new byte[digest.getDigestSize()];
        digest.doFinal(result, 0);
        return result;
    }

    public static byte[] sign(Object obj, byte[] privateKey)
            throws IOException, GeneralSecurityException {

        return sign(obj, toPrivateKey(privateKey));
    }

    public static byte[] sign(Object obj, ECPrivateKey privateKey)
            throws IOException, GeneralSecurityException {

        Signature signature = Signature.getInstance(Rules.SIGNATURE_ALGORITHM, "BC");
        signature.initSign(privateKey, SecureRandom.getInstanceStrong());
        signature.update(fingerprint(obj));
        return signature.sign();
    }

    public static boolean verify(Object obj, byte[] signature, byte[] publicKey)
            throws IOException, GeneralSecurityException {

        return verify(obj, signature, toPublicKey(publicKey));
    }

    public static boolean verify(Object obj, byte[] signature, ECPublicKey publicKey)
            throws IOException, GeneralSecurityException {

        Signature sign = Signature.getInstance(Rules.SIGNATURE_ALGORITHM, "BC");
        sign.initVerify(publicKey);
        sign.update(fingerprint(obj));
        return sign.verify(signature);
    }

}