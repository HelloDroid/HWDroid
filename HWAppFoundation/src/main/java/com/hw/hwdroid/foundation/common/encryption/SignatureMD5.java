/*
 * Copyright (c) 2015.
 * 15-12-8 下午2:05 Chen jian
 * <p/>
 * All rights reserved.
 */

package com.hw.hwdroid.foundation.common.encryption;

import android.util.Base64;

import com.hw.hwdroid.foundation.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;


/**
 * @author chenj
 * @date 2015-8-6
 */
public class SignatureMD5 {

    // public final static String encrypt_key = "ENCRYPT_KEY";

    /**
     * @param args
     */
    public static void main(String[] args) {
        String src = "Vicky";
        System.out.println(signatureForMD5(src));
        System.out.println(MyMd5.md5Byte(src));
        System.out.println(MyMd5.MD5String(src, false));
        System.out.println(StringUtils.byteArrayTo32String(MyMd5.md5Byte(src), false));
        System.out.println(MyAESHandler.byte2hex(MyMd5.md5Byte(src), false));
        System.out.println(md5AndBase64(src));
        System.out.println(base64(MyMd5.md5Byte(src)));
        System.out.println(base64("020c290593cef84aeac4ea2c269d326d"));

        // System.out.println("------");
        //
        // MyCryptoHandler cryptoHandler = new MyCryptoHandler();
        // String c = cryptoHandler.encrypt(src);
        // System.out.println(c);
        // System.out.println(cryptoHandler.decrypt(c));
    }

    /**
     * 2次签名
     * 签名规则：signature[signature+EncryptKey]
     *
     * @param src
     * @param secondEncryptKey
     * @return
     */
    public static String signatureDouble(String src, String secondEncryptKey) {
        return signatureForMD5(signatureForMD5(src) + secondEncryptKey);
    }

    /**
     * MD5加密
     *
     * @param src
     * @return
     */
    public static String signatureForMD5(String src) {
        StringBuilder sign = new StringBuilder();
        try {
            // 使用MD5对待签名串求签
            byte[] bytes = null;
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(src.getBytes("UTF-8"));

            // 将MD5输出的二进制结果转换为小写的十六进制
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(bytes[i] & 0xFF);
                if (hex.length() == 1) {
                    sign.append("0");
                }
                sign.append(hex);
            }
        } catch (GeneralSecurityException ex) {
        } catch (UnsupportedEncodingException ex2) {
        }

        return sign.toString();
    }


    /**
     * MD5 and Base64
     *
     * @param src
     * @return
     */
    public static String md5AndBase64(String src) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(src.getBytes());
            // java resouce
            // return Base64.encode(messageDigest.digest());
            return Base64.encodeToString(messageDigest.digest(), Base64.NO_WRAP);
        } catch (Exception e) {
            return src;
        }
    }

    /**
     * Base64
     *
     * @param buffer
     * @return
     */
    public static String base64(byte buffer[]) {
        try {
            // java resource
            // return Base64.encode(buffer);
            return Base64.encodeToString(buffer, Base64.NO_WRAP);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * base64
     *
     * @param hexString 十六进制字符串
     * @return
     */
    public static String base64(String hexString) {
        try {
            // java resource
            // return Base64.encode(hexStringToBytes(hexString));
            return Base64.encodeToString(StringUtils.hexStringToBytes(hexString), Base64.NO_WRAP);
        } catch (Exception e) {
            return null;
        }
    }


}
