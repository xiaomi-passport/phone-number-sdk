package com.xiaomi.phone;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtils {
    // 非对称加密密钥算法
    public static final String RSA_ALGORITHM = "RSA";
    // 加密填充方式
    public static final String ENCRYPT_TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    private static final String UTF8 = "UTF-8";

    private PublicKey mPublicKey;

    public RSAUtils(String publicKeyStr) throws RSAException {
        mPublicKey = getPublicKey(publicKeyStr);
    }

    public static PublicKey getPublicKey(String publicKeyStr) throws RSAException {
        byte[] keyBytes = Base64.decode(publicKeyStr, Base64.DEFAULT);
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw (new RSAException(e));
        } catch (InvalidKeySpecException e) {
            throw (new RSAException(e));
        }
    }

    public String decrypt(String encryptedContent, String encryptedAesKey) throws RSAUtils.RSAException, UnsupportedEncodingException, AESCoder.CipherException {
        byte[] aesKey = decrypt(encryptedAesKey);
        AESCoder aesCoder = new AESCoder(aesKey);
        return aesCoder.decrypt(encryptedContent);
    }

    private byte[] decrypt(String content) throws RSAUtils.RSAException, UnsupportedEncodingException {
        byte[] bytes = Base64.decode(content.getBytes("UTF-8"), Base64.DEFAULT);
        try {
            Cipher cipher = Cipher.getInstance(RSAUtils.ENCRYPT_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, mPublicKey);
            return cipher.doFinal(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw (new RSAUtils.RSAException(e));
        } catch (NoSuchPaddingException e) {
            throw (new RSAUtils.RSAException(e));
        } catch (InvalidKeyException e) {
            throw (new RSAUtils.RSAException(e));
        } catch (IllegalBlockSizeException e) {
            throw (new RSAUtils.RSAException(e));
        } catch (BadPaddingException e) {
            throw (new RSAUtils.RSAException(e));
        }
    }

    public static class RSAException extends Exception{
        public RSAException(Throwable throwable) {
            super(throwable);
        }
    }
}
