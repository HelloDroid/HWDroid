/*
 * Copyright (c) 2015.
 * 15-12-8 下午2:05 Chen jian
 * <p/>
 * All rights reserved.
 */

package com.hw.hwdroid.foundation.common.encryption;

import android.util.Base64;

import com.orhanobut.logger.Logger;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 加解密
 */
public class AES256Cipher {

    public static byte[] ivBytes = {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00
    };

    /**
     * AES加密编码
     *
     * @param src 加密对象
     * @param key KEY
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String AES_Encode(String src, String key) throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {

        byte[] textBytes = src.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance(MyAESHandler.CIPHER_ALGORITHM_CBC); // "AES/CBC/PKCS5Padding"
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        byte[] encryptData = cipher.doFinal(textBytes);

        return Base64.encodeToString(encryptData, Base64.NO_WRAP).trim();
    }

    /**
     * Aes加密
     *
     * @param src
     * @param key
     * @param encode 编码
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String AES_Encrypt(String src, String key, boolean encode) throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {

        byte[] textBytes = src.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance(MyAESHandler.CIPHER_ALGORITHM_CBC); // AES/CBC/PKCS5Padding
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        byte[] encryptData = cipher.doFinal(textBytes);

        Logger.d(MyAESHandler.showByteArray(encryptData));

        if (encode) {
            return Base64.encodeToString(encryptData, Base64.NO_WRAP).trim();
        }

        return MyAESHandler.byte2hex(encryptData);
    }

    /**
     * AES 解密
     *
     * @param src 密文
     * @param key Key
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String AES_Decode(String src, String key) throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {

        byte[] textBytes = Base64.decode(src, 0);
        // byte[] textBytes = str.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance(MyAESHandler.CIPHER_ALGORITHM_CBC); // "AES/CBC/PKCS5Padding"
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);

        return new String(cipher.doFinal(textBytes), "UTF-8");
    }

}
