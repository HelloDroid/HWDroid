package com.hw.hwdroid.dialog.utils;

/**
 * String基本处理方式 Utility class to peform common String manipulation algorithms.
 */
public class StringUtils {

    /**
     * 修剪字符串
     *
     * @param source
     * @return
     */
    public static String trim(String source) {
        return trim(source, true);
    }

    /**
     * 修剪字符串
     *
     * @param source
     * @param changeEmptyOrNullStr
     * @return
     */
    public static String trim(String source, boolean changeEmptyOrNullStr) {
        if (changeEmptyOrNullStr) {
            return source == null ? "" : source.trim();
        } else {
            return source == null ? null : source.trim();
        }
    }

    /**
     * 修剪字符串
     *
     * @param source
     * @return
     */
    public static String trim(CharSequence source) {
        return trim(source, true);
    }

    /**
     * 修剪字符串
     *
     * @param source
     * @param changeEmptyOrNullStr
     * @return
     */
    public static String trim(CharSequence source, boolean changeEmptyOrNullStr) {
        if (changeEmptyOrNullStr) {
            return source == null ? "" : source.toString().trim();
        } else {
            return source == null ? null : source.toString().trim();
        }
    }

    /**
     * 删除左边第一个空格
     *
     * @param source
     * @return
     */
    public static String ltrim(String source) {
        if (isNullOrWhiteSpace(source)) {
            return source;
        }

        try {
            return source.replaceFirst("^\\s", "");
        } catch (Exception e) {
        }
        return source;
    }

    /**
     * 删除右边最后一个空格
     *
     * @param source
     * @return
     */
    public static String rtrim(String source) {
        if (isNullOrWhiteSpace(source)) {
            return source;
        }

        try {
            return source.replaceFirst("\\s+$", "");
        } catch (Exception e) {
        }
        return source;
    }

    /**
     * 删除左右边最后一个空格
     *
     * @param source
     * @return
     */
    public static String lrtrim(String source) {
        if (isNullOrWhiteSpace(source)) {
            return source;
        }

        try {
            return source.replaceAll("^\\s+.*(\\s+)$", "");
        } catch (Exception e) {
        }
        return source;
    }


    /**
     * 将Null转换为""
     *
     * @param str
     * @return
     */
    public static CharSequence changeNull(CharSequence str) {
        return str == null ? "" : str;
    }

    /**
     * 将Null转换为""
     *
     * @param str
     * @return
     */
    public static String changeNull(String str) {
        return str == null ? "" : str;
    }

    /**
     * 转换空字符为“”
     *
     * @param str
     * @return
     */
    public static CharSequence changeNullOrWhiteSpace(CharSequence str) {
        return isNullOrWhiteSpace(str) ? "" : str;
    }


    /**
     * 转换空字符为“”
     *
     * @param str
     * @return
     */
    public static String changeNullOrWhiteSpace(String str) {
        return isNullOrWhiteSpace(str) ? "" : str;
    }

    /**
     * 判断字符串是否为空
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmptyOrNull(CharSequence str) {
        if (str == null || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static String getUnNullString(String inStr) {
        if (StringUtils.isEmptyOrNull(inStr)) {
            inStr = "";
        }
        return inStr;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @param trim
     * @return
     */
    public static boolean isEmptyOrNull(String str, boolean trim) {
        if (trim) {
            return isNullOrWhiteSpace(str);
        }

        return (str == null || str.length() == 0);
    }


    /**
     * 字符串经过trim处理之后是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNullOrWhiteSpace(String str) {
        return null == str || 0 == str.trim().length();
    }

    /**
     * 字符串经过trim处理之后是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNullOrWhiteSpace(CharSequence str) {
        return null == str || isNullOrWhiteSpace(str.toString());
    }

    /**
     * 判断字符串是否为空
     *
     * @param arrStr
     * @return
     */
    public static boolean isNullOrWhiteSpace(CharSequence... arrStr) {
        for (CharSequence str : arrStr) {
            if (isNullOrWhiteSpace(str)) {
                return true;
            }
        }

        return false;
    }


    /**
     * 判断字符串是否为空
     *
     * @param arrStr
     * @return
     */
    public static boolean isEmptyOrNull(CharSequence... arrStr) {
        for (CharSequence str : arrStr) {
            if (isEmptyOrNull(str)) {
                return true;
            }
        }

        return false;
    }

}
