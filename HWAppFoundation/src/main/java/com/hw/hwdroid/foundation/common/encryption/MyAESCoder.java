/*
 * Copyright (c) 2015.
 * 15-12-8 下午2:05 Chen jian
 * <p/>
 * All rights reserved.
 */

/**
 *
 */
package com.hw.hwdroid.foundation.common.encryption;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * @author chenj
 * @date 2015-8-7
 */
public class MyAESCoder {

    // private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    public static void main(String[] args) throws Exception {
        MyAESCoder myAESCoder = new MyAESCoder();

        byte[] key = myAESCoder.initSecretKey();

        String cKey = "1234567890123456";
        key = cKey.getBytes();
        System.out.println("key：" + MyAESHandler.showByteArray(key));

        Key k = MyAESHandler.toKey(key);

        String data = "AES数据";
        System.out.println("加密前数据: string:" + data);
        System.out.println("加密前数据: byte[]:" + MyAESHandler.showByteArray(data.getBytes()));
        System.out.println();

        byte[] encryptData = myAESCoder.encrypt(data.getBytes(), k);
        System.out.println("加密后数据: byte[]:" + MyAESHandler.showByteArray(encryptData));
        //            System.out.println("加密后数据: hexStr:" + Hex.encodeHexStr(encryptData));
        System.out.println();


        System.out.println(MyAESHandler.encrypt(false, data, cKey, MyAESHandler.byte2hex(AES256Cipher.ivBytes), ""));

        byte[] decryptData = myAESCoder.decrypt(encryptData, k);
        System.out.println("解密后数据: byte[]:" + MyAESHandler.showByteArray(decryptData));
        System.out.println("解密后数据: string:" + new String(decryptData));

        System.out.println(AES256Cipher.AES_Encrypt(data, cKey, false));
        System.out.println(AES256Cipher.AES_Encode(data, cKey));
    }

    /**
     * 初始化密钥
     *
     * @return byte[]        密钥
     * @throws Exception
     */
    public byte[] initSecretKey() {
        // 返回生成指定算法的秘密密钥的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            return new byte[0];
        }
        // 初始化此密钥生成器，使其具有确定的密钥大小
        // AES 要求密钥长度为 128
        kg.init(128);
        // 生成一个密钥
        SecretKey secretKey = kg.generateKey();

        return secretKey.getEncoded();
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[]    加密数据
     * @throws Exception
     */
    public byte[] encrypt(byte[] data, Key key) throws Exception {
        return encrypt(data, key, "AES/ECB/PKCS5Padding");
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  二进制密钥
     * @return byte[]    加密数据
     * @throws Exception
     */
    public byte[] encrypt(byte[] data, byte[] key) throws Exception {
        return encrypt(data, key, "AES/ECB/PKCS5Padding");
    }

    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             二进制密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]        加密数据
     * @throws Exception
     */
    public byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
        // 还原密钥
        Key k = MyAESHandler.toKey(key);
        return encrypt(data, k, cipherAlgorithm);
    }

    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]         加密数据
     * @throws Exception
     */
    public byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
        // 实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // 使用密钥初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  二进制密钥
     * @return byte[]    解密数据
     * @throws Exception
     */
    public byte[] decrypt(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, "AES/ECB/PKCS5Padding");
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[]    解密数据
     * @throws Exception
     */
    public byte[] decrypt(byte[] data, Key key) throws Exception {
        return decrypt(data, key, "AES/ECB/PKCS5Padding");
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             二进制密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]        解密数据
     * @throws Exception
     */
    public byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
        // 还原密钥
        Key k = MyAESHandler.toKey(key);
        return decrypt(data, k, cipherAlgorithm);
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]        解密数据
     * @throws Exception
     */
    public byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
        // 实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // 使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 执行操作
        return cipher.doFinal(data);
    }

}
