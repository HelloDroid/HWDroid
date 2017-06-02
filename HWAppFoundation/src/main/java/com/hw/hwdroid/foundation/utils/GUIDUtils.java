package com.hw.hwdroid.foundation.utils;

import java.util.UUID;

/**
 * A GUID tool. This tool class exists to separate dependency of xNote code from
 * the underlying implementation of GUID generator.
 */
public class GUIDUtils {

    /**
     * @return a guid
     */
    public static String guid() {
        // return UUID.randomUUID().toString();

        return uuid(false, false);
    }

    /**
     * uuid
     * <p/>
     * 550e8400-e29b-41d4-a716-446655440000
     *
     * @param hyphen      去掉连号
     * @param toUpperCase 转换为大写，false:保持不变
     * @return
     */
    public static String uuid(boolean hyphen, boolean toUpperCase) {
        UUID uuid = UUID.randomUUID();

        if (null == uuid) {
            return "";
        }

        String uuidStr = uuid.toString();

        if (null == uuidStr || uuidStr.length() == 0) {
            return uuidStr;
        }

        if (hyphen) {
            uuidStr = uuidStr.replaceAll("-", "");
        }

        return toUpperCase ? uuidStr.toUpperCase() : uuidStr;
    }

    /**
     * uuid
     *
     * @param hyphen      去掉连号
     * @param toLowerCase 转换为小写，false:保持不变
     * @return
     */
    public static String uuid2(boolean hyphen, boolean toLowerCase) {
        UUID uuid = UUID.randomUUID();

        if (null == uuid) {
            return "";
        }

        String uuidStr = uuid.toString();

        if (null == uuidStr || uuidStr.length() == 0) {
            return uuidStr;
        }

        if (hyphen) {
            uuidStr = uuidStr.replaceAll("-", "");
        }

        return toLowerCase ? uuidStr.toLowerCase() : uuidStr;
    }

}
