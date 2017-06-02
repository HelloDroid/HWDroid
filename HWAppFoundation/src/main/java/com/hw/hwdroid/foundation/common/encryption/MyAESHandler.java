
package com.hw.hwdroid.foundation.common.encryption;

import android.util.Base64;

import com.hw.hwdroid.foundation.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author chenj
 * @date 2015-8-7
 */
public class MyAESHandler {

    public static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";
    public static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";

    /**
     * @param args
     */
    public static void main(String[] args) {
        /*
         * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
		 * 此处使用AES-128-CBC加密模式，key需要为16位。
		 */
        String cKey = "1234567890123456";
        // 需要加密的字串
        String cSrc = "Email : arix04@xxx.com";
        System.out.println(cSrc);

        String iv = "0102030405060708";

        String enPassword = MyAESHandler.encrypt(true, "24743zj", "1234567890654321", "0102030404030201", MyAESHandler.CIPHER_ALGORITHM_CBC);
        System.out.println("enPassword：" + enPassword + "enPassword");

        // 加密
        long lStart = System.currentTimeMillis();
        String enString = encrypt(true, cSrc, cKey, iv, CIPHER_ALGORITHM_CBC);
        System.out.println("加密后的字串是：" + enString);

        long lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("加密耗时：" + lUseTime + "毫秒");

        // 解密
        lStart = System.currentTimeMillis();
        String DeString = decrypt(true, enString, cKey, iv, CIPHER_ALGORITHM_CBC);
        System.out.println("解密后的字串是：" + DeString);
        lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("解密耗时：" + lUseTime + "毫秒");

        System.out.println(encrypt2Hex(cSrc, cKey, "0102030405060708"));
        System.out.println(decryptFromHex("503d775049d391e535eef8deb123ddbc62dce159ce649cc270e42f0fc91d4770", cKey, "0102030405060708"));
    }

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return 密钥
     */
    public static Key toKey(byte[] key) {
        // 生成密钥
        return new SecretKeySpec(key, "AES");
    }

    /**
     * 转换密钥
     *
     * @param key 密钥，长度为16
     * @return
     */
    public static Key toKey(String key) {
        return new SecretKeySpec(key.getBytes(), "AES");
    }

    /**
     * 加密
     *
     * @param src 需要加密的字串
     * @param key 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     *            此处使用AES-128-CBC加密模式，key需要为16位
     * @return
     */
    public static String encrypt(String src, String key) {
        return encrypt(src, key, byte2hex(AES256Cipher.ivBytes));
    }

    /**
     * 加密
     *
     * @param src 需要加密的字串
     * @param key 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     *            此处使用AES-128-CBC加密模式，key需要为16位
     * @param iv  向量iv，可增加加密算法的强度，iv需要为16位
     * @return
     */
    public static String encrypt(String src, String key, String iv) {
        return encrypt(true, src, key, iv);
    }

    /**
     * 加密
     *
     * @param src     需要加密的字串
     * @param key     加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     *                此处使用AES-128-CBC加密模式，key需要为16位
     * @param ivBytes 向量iv，可增加加密算法的强度，iv需要为16位
     * @return
     */
    public static String encrypt(String src, String key, byte[] ivBytes) {
        return encrypt(true, src, key, ivBytes, CIPHER_ALGORITHM_CBC);
    }

    /**
     * 加密(用byte2hex做转码)
     *
     * @param src 需要加密的字串
     * @param key 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     *            此处使用AES-128-CBC加密模式，key需要为16位
     * @param iv  向量iv，可增加加密算法的强度，iv需要为16位
     * @return
     */
    public static String encrypt2Hex(String src, String key, String iv) {
        return encrypt(false, src, key, iv);
    }

    /**
     * 加密
     *
     * @param isBase64 true:使用Base64做转码以及辅助加密；false:使用byte2hex做转码
     * @param src      需要加密的字串
     * @param key      加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     *                 此处使用AES-128-CBC加密模式，key需要为16位
     * @param iv       向量iv，可增加加密算法的强度，iv需要为16位
     * @return
     */
    public static String encrypt(boolean isBase64, String src, String key, String iv) {
        return encrypt(isBase64, src, key, iv, CIPHER_ALGORITHM_CBC);
    }

    /**
     * 加密
     *
     * @param isBase64        true:使用Base64做转码以及辅助加密；false:使用byte2hex做转码
     * @param src             需要加密的字串
     * @param key             加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     *                        此处使用AES-128-CBC加密模式，key需要为16位
     * @param iv              向量iv，可增加加密算法的强度，iv需要为16位
     * @param cipherAlgorithm "算法/模式/补码方式"	如：AES/CBC/PKCS5Padding
     * @return
     */
    public static String encrypt(boolean isBase64, String src, String key, String iv, String cipherAlgorithm) {
        if (key == null) {
            return "";
        }

        // 判断Key是否为16位
        if (key.length() != 16) {
            return "";
        }

        // "算法/模式/补码方式"
        if (null == cipherAlgorithm || cipherAlgorithm.length() == 0) {
            if (iv != null && iv.length() == 16) {
                cipherAlgorithm = CIPHER_ALGORITHM_CBC;
            } else {
                cipherAlgorithm = CIPHER_ALGORITHM_ECB;
            }
        }

        try {
            SecretKeySpec newKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);

            // "算法/模式/补码方式"
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            // IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
            // IvParameterSpec ivSpec = new IvParameterSpec("0102030405060708".getBytes());
            // IvParameterSpec ivSpec = new IvParameterSpec(key.getBytes());

            // 没有向量使用ECB模式
            if (iv != null && iv.length() == 16) {
                IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
                cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, newKey);
            }

            byte[] encrypted = cipher.doFinal(src.getBytes());

            Logger.d(MyAESHandler.showByteArray(encrypted));

            // 使用BASE64做转码功能，同时能起到2次加密的作用
            if (isBase64) {
                // android soucrce
                return Base64.encodeToString(encrypted, Base64.NO_WRAP);
                // java source
                // return new BASE64Encoder().encode(encrypted);
            }
            // 使用byte2hex做转码
            else {
                return byte2hex(encrypted).toLowerCase();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 加密
     *
     * @param isBase64 true:使用Base64做转码以及辅助加密；false:使用byte2hex做转码
     * @param src      需要加密的字串
     * @param key      key需要为16位.    可由UUID转byte，StringUtils.hex2byte(uuid)
     * @param ivx      向量iv，可增加加密算法的强度，iv需要为16位
     * @return
     */
    public static String encryptAESCBCPKCS5Padding(boolean isBase64, String src, byte[] key, byte[] ivx) {
        if (key == null || null == src || src.length() == 0) {
            return src;
        }

        if (null == key || null == ivx || 0 == key.length || 0 == ivx.length) {
            return src;
        }

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivx);
            Cipher cipher = Cipher.getInstance(MyAESHandler.CIPHER_ALGORITHM_CBC);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);

            byte[] encrypted = cipher.doFinal(src.getBytes("UTF-8"));

            Logger.d(MyAESHandler.showByteArray(encrypted));

            // 使用BASE64做转码功能，同时能起到2次加密的作用
            if (isBase64) {
                return Base64.encodeToString(encrypted, Base64.NO_WRAP);
            }
            // 使用byte2hex做转码
            else {
                return byte2hex(encrypted).toLowerCase();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 加密
     *
     * @param isBase64        true:使用Base64做转码以及辅助加密；false:使用byte2hex做转码
     * @param src             需要加密的字串
     * @param key             加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     *                        此处使用AES-128-CBC加密模式，key需要为16位
     * @param ivBytes         向量iv，可增加加密算法的强度，iv需要为16位
     * @param cipherAlgorithm "算法/模式/补码方式"	如：AES/CBC/PKCS5Padding
     * @return
     */
    public static String encrypt(boolean isBase64, String src, String key, byte[] ivBytes, String cipherAlgorithm) {
        if (key == null) {
            return "";
        }

        // 判断Key是否为16位
        if (key.length() != 16) {
            return "";
        }

        // "算法/模式/补码方式"
        if (null == cipherAlgorithm || cipherAlgorithm.length() == 0) {
            if (ivBytes != null && ivBytes.length > 0) {
                cipherAlgorithm = CIPHER_ALGORITHM_CBC;
            } else {
                cipherAlgorithm = CIPHER_ALGORITHM_ECB;
            }
        }

        try {
            SecretKeySpec newKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);

            // 没有向量使用ECB模式
            if (ivBytes != null && ivBytes.length > 0) {
                IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
                cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, newKey);
            }

            byte[] encrypted = cipher.doFinal(src.getBytes());

            Logger.d(MyAESHandler.showByteArray(encrypted));

            // 使用BASE64做转码功能，同时能起到2次加密的作用
            if (isBase64) {
                return Base64.encodeToString(encrypted, Base64.NO_WRAP);
            }
            // 使用byte2hex做转码
            else {
                return byte2hex(encrypted).toLowerCase();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return "";
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
        return encrypt(data, key, CIPHER_ALGORITHM_ECB);
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  二进制密钥
     * @return byte[]    加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        return encrypt(data, key, CIPHER_ALGORITHM_ECB);
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
    public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
        // 还原密钥
        Key k = toKey(key);
        return encrypt(data, k, cipherAlgorithm);
    }

    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]        加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
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
     * @param encryptSrc 密文
     * @param key        加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     *                   此处使用AES-128-CBC加密模式，key需要为16位。
     * @return
     */
    public static String decrypt(String encryptSrc, String key) {
        return decrypt(encryptSrc, key, "");
    }

    /**
     * 解密
     *
     * @param encryptSrc 密文
     * @param key        加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     *                   此处使用AES-128-CBC加密模式，key需要为16位
     * @param iv         向量iv，可增加加密算法的强度，iv需要为16位
     * @return
     */
    public static String decrypt(String encryptSrc, String key, String iv) {
        return decrypt(true, encryptSrc, key, iv);
    }

    /**
     * 解密(用byte2hex做转码)
     *
     * @param encryptSrc 密文
     * @param key        加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     *                   此处使用AES-128-CBC加密模式，key需要为16位
     * @param iv         向量iv，可增加加密算法的强度，iv需要为16位
     * @return
     */
    public static String decryptFromHex(String encryptSrc, String key, String iv) {
        return decrypt(false, encryptSrc, key, iv);
    }

    /**
     * 解密
     *
     * @param isBase64   true:使用Base64做转码以及辅助加密；false:使用byte2hex做转码
     * @param encryptSrc 密文
     * @param key        加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     *                   此处使用AES-128-CBC加密模式，key需要为16位
     * @param iv         向量iv，可增加加密算法的强度，iv需要为16位
     * @return
     */
    public static String decrypt(boolean isBase64, String encryptSrc, String key, String iv) {
        return decrypt(isBase64, encryptSrc, key, iv, CIPHER_ALGORITHM_CBC);
    }

    /**
     * 解密
     *
     * @param isBase64        true:使用Base64做转码以及辅助加密；false:使用byte2hex做转码
     * @param encryptSrc      密文
     * @param key             加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
     *                        此处使用AES-128-CBC加密模式，key需要为16位
     * @param iv              向量iv，可增加加密算法的强度，iv需要为16位
     * @param cipherAlgorithm "算法/模式/补码方式"	如：AES/CBC/PKCS5Padding
     * @return
     */
    public static String decrypt(boolean isBase64, String encryptSrc, String key, String iv, String cipherAlgorithm) {
        try {
            // 判断Key是否正确
            if (key == null) {
                return "";
            }

            // 判断Key是否为16位
            if (key.length() != 16) {
                return "";
            }

            if (null == cipherAlgorithm || cipherAlgorithm.length() == 0) {
                if (iv != null && iv.length() == 16) {
                    cipherAlgorithm = CIPHER_ALGORITHM_CBC;
                } else {
                    cipherAlgorithm = CIPHER_ALGORITHM_ECB;
                }
            }

            // getBytes("ASCII")
            byte[] raw = key.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);

            // IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
            // IvParameterSpec ivSpec = new IvParameterSpec(key.getBytes());
            // IvParameterSpec ivSpec = new IvParameterSpec("0102030405060708".getBytes());

            // 没有向量使用ECB模式
            if (null != iv && iv.length() == 16) {
                IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            }

            byte[] encrypted1 = null;

            if (isBase64) {
                // 先用base64解密
                // android soucrce
                encrypted1 = Base64.decode(encryptSrc, Base64.NO_WRAP);
                // java source
                // encrypted1 = new BASE64Decoder().decodeBuffer(encryptSrc);
            } else {
                encrypted1 = hex2byte(encryptSrc);
            }

            byte[] original = cipher.doFinal(encrypted1);
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  二进制密钥
     * @return byte[]    解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, CIPHER_ALGORITHM_ECB);
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[]    解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key) throws Exception {
        return decrypt(data, key, CIPHER_ALGORITHM_ECB);
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
    public static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
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
    public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
        // 实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // 使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * hex to byte
     *
     * @param hexStr
     * @return
     */
    public static byte[] hex2byte(String hexStr) {
        return StringUtils.hex2byte(hexStr);
    }

    /**
     * byte to hex
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        return byte2hex(b, true);
    }

    /**
     * byte to hex
     *
     * @param b
     * @param toUpperCase
     * @return
     */
    public static String byte2hex(byte[] b, boolean toUpperCase) {
        return StringUtils.byte2hex(b, toUpperCase);
    }

    /**
     * print byte
     *
     * @param data
     * @return
     */
    public static String showByteArray(byte[] data) {
        return StringUtils.printByteArray(data);
    }
}
