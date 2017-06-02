package com.hw.hwdroid.foundation.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 金额格式化
 * <p/>
 * Created by chen.jiana on 2015/9/9.
 */
public class MoneyUtils {

    /**
     * 金额格式化
     *
     * @param money 金额
     * @param len   小数位数
     * @return 格式后的金额
     */
    public static String insertComma(String money, int len) {
        if (money == null || money.length() < 1) {
            return "";
        }

        try {
            NumberFormat formater = null;
            double num = Double.parseDouble(money);
            if (len == 0) {
                formater = new DecimalFormat("###,###");
            } else {
                StringBuffer buff = new StringBuffer();
                buff.append("###,###.");
                for (int i = 0; i < len; i++) {
                    buff.append("#");
                }
                formater = new DecimalFormat(buff.toString());
            }

            return formater.format(num);
        } catch (Exception e) {
            return money;
        }
    }

    /**
     * 保留2位小数的金额格式化
     *
     * @param money
     * @param defValue
     * @return
     */
    public static String insertCommaTwoDecimals(String money, String defValue) {
        String newMoney = insertComma(money, 2);

        if (null == newMoney) {
            newMoney = "";
        }

        if ("".equals(newMoney) || "0.0".equals(newMoney) || "0.00".equals(newMoney)) {
            newMoney = defValue;
        }

        return newMoney;
    }

    /**
     * 金额格式化(不适用逗号)
     *
     * @param money
     * @param len
     * @return
     */
    public static String formatterMoney(String money, int len) {
        if (money == null || money.length() < 1) {
            return "";
        }

        try {
            NumberFormat formater = null;
            double num = Double.parseDouble(money);
            if (len == 0) {
                formater = new DecimalFormat("###");
            } else {
                StringBuffer buff = new StringBuffer();
                buff.append("###.");
                for (int i = 0; i < len; i++) {
                    buff.append("#");
                }
                formater = new DecimalFormat(buff.toString());
            }

            return formater.format(num);
        } catch (Exception e) {
            return money;
        }
    }

    /**
     * 保留2位小数的金额格式化(不适用逗号)
     *
     * @param money
     * @param defValue
     * @return
     */
    public static String formatterMoneyTwoDecimals(String money, String defValue) {
        String newMoney = formatterMoney(money, 2);

        if (null == newMoney) {
            newMoney = "";
        }

        if ("".equals(newMoney) || "0.0".equals(newMoney) || "0.00".equals(newMoney)) {
            newMoney = defValue;
        }

        return newMoney;
    }

    /**
     * 金额去掉“,”
     *
     * @param s 金额
     * @return 去掉“,”后的金额
     */
    public static String delComma(String s) {
        String formatString = "";
        if (s != null && s.length() >= 1) {
            formatString = s.replaceAll(",", "");
        }

        return formatString;
    }

}
