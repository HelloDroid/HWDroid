/*
 * Copyright (c) 2015.
 * 15-12-8 下午2:05 Chen jian
 * <p/>
 * All rights reserved.
 */

package com.hw.hwdroid.foundation.common.encryption;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author chenj
 * @date 2014-12-13
 */
@Deprecated
public class MyCryptoHandler {

    // Your IV Initialization Vector
    final static String KEY_HEX = "82a45dd4f33c327ac35eabd52c96e1831a2e940d8730f20982472d049021de98";
    final static String IV_HEX = "73af90672312ec5e56055d60fd1f8855";

    private byte[] key;
    private byte[] ivx;

    public MyCryptoHandler() {
        try {
            ivx = hexStringToBytes(IV_HEX);
            // Your Secret Key
            key = hexStringToBytes(KEY_HEX);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MyCryptoHandler(String hexKey, String hexIv) {
        try {
            ivx = hexStringToBytes(hexKey);
            // Your Secret Key
            key = hexStringToBytes(hexIv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密
     *
     * @param message
     * @return
     */
    public String encrypt(String message) {
        String base64 = "";
        try {
            byte[] srcBuff = message.getBytes("UTF-8");

            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivx);
            Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);

            byte[] dstBuff = ecipher.doFinal(srcBuff);

            // android Base64
            base64 = Base64.encodeToString(dstBuff, Base64.NO_WRAP);
            // java source
            // base64 = Base64.encode(dstBuff);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return base64;

    }

    /**
     * 解密
     *
     * @param encrypted
     * @return
     */
    public String decrypt(String encrypted) {
        String original = "";
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivx);

            Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);

            // android Base64
            byte[] raw = Base64.decode(encrypted, Base64.NO_WRAP);
            // java source
            // byte[] raw = Base64.decode(encrypted);
            byte[] originalBytes = ecipher.doFinal(raw);

            original = new String(originalBytes, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return original;
    }

    /**
     * Convert hex string to byte[]   把为字符串转化为字节数组
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
