package com.xiaomi.phone;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

public class AESCoder {
    public static final String TAG = "AESCoder";

    public static final String AES_ALGORITHM = "AES";

    private static final String UTF8 = "UTF-8";

    private SecretKeySpec keySpec;

    public AESCoder(byte[] aesKey) {
        if (aesKey == null) {
            throw new SecurityException("aes key is null");
        }

        if (aesKey.length != 16) {
            throw new SecurityException("aesKey is invalid");
        }
        keySpec = new SecretKeySpec(aesKey, "AES");
    }

    public String decrypt(String base64EncodeData) throws CipherException {
        if (base64EncodeData == null) {
            throw new SecurityException("decrypt failed for empty data");
        }
        try {
            byte[] encryptedByte = Base64.decode(base64EncodeData, Base64.NO_WRAP);
            byte[] decryptedByte = decrypt(encryptedByte);
            return new String(decryptedByte, "UTF-8");
        } catch (Exception e) {
            // catch all exceptions including runtime exceptions
            // Here all failures are regarded as cipher failure, means cipher exception
            throw new CipherException("fail to decrypt by aescoder", e);
        }
    }

    private byte[] decrypt(byte[] cipherData) throws CipherException {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            if (null == cipherData) {
                throw new IllegalBlockSizeException("no block data");
            }
            byte[] rawData = cipher.doFinal(cipherData);
            return rawData;
        } catch (Exception e) {
            // catch all exceptions including runtime exceptions
            // Here all failures are regarded as cipher failure, means cipher exception
            throw new CipherException("fail to decrypt by aescoder", e);
        }
    }

    public class CipherException extends Exception {

        public CipherException(String msg) {
            super(msg);
        }

        public CipherException(String message, Throwable cause) {
            super(message, cause);
        }

        public CipherException(Throwable cause) {
            super(cause);
        }
    }
}
