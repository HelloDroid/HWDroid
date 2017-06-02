package com.hw.hwdroid.foundation.common.encryption;

import com.hw.hwdroid.foundation.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 */
public class MyMd5 {

    /**
     * MD5加密
     *
     * @param str 要加密的字符串
     * @return String 加密的字符串
     */
    public final static String MD5(String str) {
        return MD5(str, false);
    }

    /**
     * MD5加密
     *
     * @param str 要加密的字符串
     * @return String 加密的字符串
     */
    public final static String MD5(String str, boolean toUpperCase) {
        // 用来将字节转换成 16 进制表示的字符
        char hexDigits[] = StringUtils.hexDigitsChar;

        try {
            byte[] strByteArr = str.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(strByteArr);
            byte[] digest = md5.digest(); // MD5 的计算结果是一个 128 位的长整数，

            // 用字节表示就是 16 个字节，每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
            char[] strs = new char[16 * 2];
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节，转换成 16
                // 进制字符的转换
                byte byte0 = digest[i]; // 取第 i 个字节
                strs[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>
                // 为逻辑右移，将符号位一起右移
                strs[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            // return new String(strs).toUpperCase(); // 换后的结果转换为字符串
            if (toUpperCase) {
                return String.valueOf(strs).toUpperCase();
            } else {
                return String.valueOf(strs);
            }
        } catch (Exception e) {
            return str;
        }
    }

    public static String MD5_32(String str) {
        if (StringUtils.isEmptyOrNull(str)) {
            return "";
        }

        try {
            return MD5_32(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Logger.e(e);
        }
        return str;
    }

    public static String MD5_32(byte[] strByteArr) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            StringBuffer strBuf = new StringBuffer();
            md5.update(strByteArr);
            byte[] digest = md5.digest();
            for (int i = 0; i < digest.length; i++) {
                strBuf.append(StringUtils.byte2Hex_toLowerCase(digest[i]));
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            Logger.e(e);
        }

        return "";
    }

    public static String MD5_16(String str) {
        if (StringUtils.isEmptyOrNull(str)) {
            return "";
        }

        return MD5_32(str).subSequence(8, 24).toString();
    }

    public static String MD5_16(byte[] strByteArr) {
        return MD5_32(strByteArr).subSequence(8, 24).toString();
    }


    /**
     * MD5加密
     *
     * @param src
     * @return byte[]
     */
    public static byte[] md5Byte(String src) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(src.getBytes("UTF-8"));
            return messageDigest.digest();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    public final static String MD5String(String str, boolean toUpperCase) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes("UTF-8"));
            String pwd = new BigInteger(1, messageDigest.digest()).toString(16);

            if (toUpperCase) {
                return pwd.toUpperCase(); // 换后的结果转换为字符串
            }

            return pwd;
        } catch (Exception e) {
            return str;
        }
    }

    /**
     * SHA 加密
     *
     * @param str
     * @return
     */
    public final static String SHA(String str) {
        try {
            byte[] strTemp = str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("SHA");
            mdTemp.update(strTemp);
            byte tmp[] = mdTemp.digest(); // MD5 的计算结果是一个 128 位的长整数，

            // 用字节表示就是 16 个字节，每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
            char strs[] = new char[16 * 2];
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节，转换成 16
                // 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                strs[k++] = StringUtils.hexDigitsChar[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>
                // 为逻辑右移，将符号位一起右移
                strs[k++] = StringUtils.hexDigitsChar[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            return String.valueOf(strs).toUpperCase(); // 换后的结果转换为字符串
        } catch (Exception e) {
            return str;
        }
    }

    /**
     * MD5加密
     *
     * @param strObj
     * @return
     */
    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = StringUtils.byteArrayToHexString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            return strObj;
        }

        return resultString.toUpperCase();
    }

    /**
     * The main method
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        System.out.println(MD5("2011123456").toLowerCase());

        String s = "http://lsd2113821830.blog.163.com/blog/static/17234105620117104948646/";
        System.out.println(s);
        System.out.println(MD5(s));

        System.out.println(s);
        System.out.println(MD5String(s, false));

        System.out.println("-----------");
        System.out.println(GetMD5Code(s));
    }

}
