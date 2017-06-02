/*
 * Copyright (c) 2015.
 * 15-12-8 上午10:44 Chen jian
 * <p/>
 * All rights reserved.
 */

package com.hw.hwdroid.foundation.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * TelephonyManager相关工具类
 */
public class TelephonyUtils {
    private final static String CHINA_MOBILE = "中国移动";

    private final static String CHINA_UNICOM = "中国联通";

    private final static String CHINA_TELECOM = "中国电信";

    /**
     * 获取客户ID（在gsm中是imsi号）
     *
     * @param context 应用环境
     * @return ID（imsi）
     */
    public static String getSubscriberId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSubscriberId();
    }

    /**
     * IMEI
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getProviderName(Context context) {
        String provider = "";
        String imsi = getSubscriberId(context);
        if (imsi == null) {
            return "";
        }
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
            provider = CHINA_MOBILE;
        } else if (imsi.startsWith("46001")) {
            provider = CHINA_UNICOM;
        } else if (imsi.startsWith("46003")) {
            provider = CHINA_TELECOM;
        }
        return provider;
    }
}
