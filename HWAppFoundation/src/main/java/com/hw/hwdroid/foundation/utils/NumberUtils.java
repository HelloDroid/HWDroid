package com.hw.hwdroid.foundation.utils;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Number Utils
 *
 * @author chenjian
 * @date 2014-4-11
 */

public class NumberUtils {

    public final static DecimalFormat df = new DecimalFormat("#############.##");
    public final static DecimalFormat df_int = new DecimalFormat("#############");

    /**
     * to Boolean
     *
     * @param s
     * @return
     */
    public static boolean parseBoolean(String s) {
        return parseBoolean(s, false);
    }

    /**
     * to Boolean
     *
     * @param s
     * @param defaultV 默认值
     * @return
     */
    @NonNull
    public static boolean parseBoolean(String s, boolean defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Boolean.parseBoolean(s);
        } catch (Exception ex) {
            Logger.e(ex);
        }
        return defaultV;
    }

    /**
     * to Boolean Object
     *
     * @param s
     * @return
     */
    public static Boolean parseBoolObj(String s) {
        return parseBoolObj(s, Boolean.FALSE);
    }

    /**
     * to Boolean Object
     *
     * @param s
     * @param defaultV
     * @return
     */
    public static Boolean parseBoolObj(String s, Boolean defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Boolean.parseBoolean(s) ? Boolean.TRUE : Boolean.FALSE;
        } catch (Exception ex) {
            Logger.e(ex);
        }

        return defaultV;
    }

    /**
     * to Int
     *
     * @param s
     * @return
     */
    public static int parseInt(String s) {
        return parseInt(s, 0);
    }

    /**
     * to Int
     *
     * @param s
     * @param defaultV 默认值
     * @return
     */
    public static int parseInt(String s, int defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Integer.parseInt(s);
        } catch (Exception ex) {
            Logger.e(ex);
        }

        return defaultV;
    }

    /**
     * to Integer
     *
     * @param s
     * @return
     */
    public static Integer parseIntegerObj(String s) {
        return parseIntegerObj(s, null);
    }

    /**
     * to Integer
     *
     * @param s
     * @param defaultV
     * @return
     */
    public static Integer parseIntegerObj(String s, Integer defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Integer.valueOf(s);
        } catch (Exception ex) {
            Logger.e(ex);
        }

        return defaultV;
    }

    /**
     * to Short
     *
     * @param s
     * @param s 默认值
     * @return
     */
    public static short parseShort(String s) {
        return parseShort(s, (short) 0);
    }

    /**
     * to Short
     *
     * @param s
     * @param defaultV 默认值
     * @return
     */
    public static short parseShort(String s, short defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Short.parseShort(s);
        } catch (Exception ex) {
            Logger.e(ex);
            return defaultV;
        }
    }

    public static Short parseShortObj(String s) {
        return parseShortObj(s, null);
    }

    public static Short parseShortObj(String s, Short defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Short.valueOf(s);
        } catch (Exception ex) {
            Logger.e(ex);
            return defaultV;
        }
    }

    /**
     * to Long
     *
     * @param s
     * @return
     */
    public static long parseLong(String s) {
        return parseLong(s, 0);
    }

    /**
     * to Long
     *
     * @param s
     * @param defaultV 默认值
     * @return
     */
    public static long parseLong(String s, long defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Long.parseLong(s);
        } catch (Exception ignored) {
            Logger.e(ignored);
        }

        return defaultV;
    }

    public static long parseLongObj(String s) {
        return parseLongObj(s, null);
    }

    public static Long parseLongObj(String s, Long defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Long.valueOf(s);
        } catch (Exception ignored) {
            Logger.e(ignored);
        }

        return defaultV;
    }

    /**
     * to Double
     *
     * @param s
     * @return
     */
    public static double parseDouble(String s) {
        return parseDouble(s, 0);
    }

    /**
     * to Double
     *
     * @param s
     * @param defaultV
     * @return
     */
    public static double parseDouble(String s, double defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Double.parseDouble(s);
        } catch (Exception ignored) {
            Logger.e(ignored);
        }

        return defaultV;
    }

    public static Double parseDoubleObj(String s) {
        return parseDoubleObj(s, null);
    }

    public static Double parseDoubleObj(String s, Double defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Double.valueOf(s);
        } catch (Exception ignored) {
            Logger.e(ignored);
        }

        return defaultV;
    }

    /**
     * to Float
     *
     * @param s
     * @return
     */
    public static float parseFloat(String s) {
        return parseFloat(s, 0f);
    }

    /**
     * to Float
     *
     * @param s
     * @param defaultV
     * @return
     */
    public static float parseFloat(String s, float defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Float.parseFloat(s);
        } catch (Exception ignored) {
            Logger.e(ignored);
        }

        return defaultV;
    }

    public static Float parseFloatObj(String s) {
        return parseFloatObj(s, null);
    }

    public static Float parseFloatObj(String s, Float defaultV) {
        try {
            if (StringUtils.isNullOrWhiteSpace(s)) {
                return defaultV;
            }

            return Float.valueOf(s);
        } catch (Exception ignored) {
            Logger.e(ignored);
        }

        return defaultV;
    }

    /**
     * boolean to int
     *
     * @param b
     * @return
     */
    public static int bool2Int(boolean b) {
        return b ? 1 : 0;
    }

    /**
     * 判断整数是否大于0
     *
     * @param i
     * @return
     */
    public static boolean int2Bool(int i) {
        //        return i >= 1;
        return i != 0;
    }


    /**
     * 判断number参数是否是整型数表示方式
     *
     * @param number
     * @return
     */
    public static boolean isIntegerNumber(String number) {
        number = number.trim();

        // 整数的正则表达式
        String intNumRegex = "\\-{0,1}\\d+";
        return number.matches(intNumRegex);
    }

    /**
     * 判断number参数是否是浮点数表示方式
     *
     * @param number
     * @return
     */
    public static boolean isFloatPointNumber(String number) {
        if (StringUtils.isNullOrWhiteSpace(number)) {
            return false;
        }

        number = number.trim();

        // 浮点数的正则表达式-小数点在中间与前面
        String pointPrefix = "(\\-|\\+){0,1}\\d*\\.\\d+";

        // 浮点数的正则表达式-小数点在后面
        String pointSuffix = "(\\-|\\+){0,1}\\d+\\.";
        if (number.matches(pointPrefix) || number.matches(pointSuffix)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 百分比模式
     *
     * @param number
     * @param decimals
     * @return
     */
    public static String percentFormat(double number, int decimals) {
        NumberFormat num = NumberFormat.getPercentInstance();
        //        num.setMaximumIntegerDigits(3);

        // 设置百分数精确度2即保留两位小数
        //        num.setMaximumFractionDigits(2);
        num.setMaximumFractionDigits(decimals);

        return num.format(number);
    }

    /**
     * 百分比模式
     *
     * @param numberStr
     * @param decimals
     * @return
     */
    public static String toPercentFormat(String numberStr, int decimals) {
        NumberFormat num = NumberFormat.getPercentInstance();
        //        num.setMaximumIntegerDigits(3);

        // 设置百分数精确度2即保留两位小数
        //        num.setMaximumFractionDigits(2);
        num.setMaximumFractionDigits(decimals);

        return num.format(parseDouble(numberStr));
    }

    /**
     * 去掉小数点后面不需要的0
     *
     * @param floatSrc
     * @return
     */
    public static String redundanceZero(String floatSrc) {
        try {
            if (StringUtils.isNullOrWhiteSpace(floatSrc)) {
                return floatSrc;
            }

            if (floatSrc.indexOf(".") > 0) {
                // 正则表达
                // 去掉后面无用的零
                floatSrc = floatSrc.replaceAll("0+?$", "");
                // 如小数点后面全是零则去掉小数点
                floatSrc = floatSrc.replaceAll("[.]$", "");
            }
            return floatSrc;
        } catch (Exception e) {
            Logger.e(e);
        }

        return floatSrc;
    }

    /**
     * 固定小数位数
     *
     * @param value
     * @param digits
     * @return
     */
    public static String setFractionDigits(double value, int digits) {
        try {
            NumberFormat format = NumberFormat.getNumberInstance();
            format.setGroupingUsed(false);
            format.setMaximumFractionDigits(digits);
            format.setMinimumFractionDigits(digits);
            return format.format(value);
        } catch (Exception e) {
            Logger.e(e);
        }

        return String.valueOf(value);
    }

    /**
     * 浮动小数位数
     *
     * @param value
     * @param minDigits
     * @param maxDigits
     * @return
     */
    public static String setFractionDigits(double value, int minDigits, int maxDigits) {
        try {
            NumberFormat format = NumberFormat.getNumberInstance();
            format.setGroupingUsed(false);
            format.setMaximumFractionDigits(maxDigits);
            format.setMinimumFractionDigits(minDigits);
            return format.format(value);
        } catch (Exception e) {
            Logger.e(e);
        }

        return String.valueOf(value);
    }

    /**
     * 格式化浮点数
     *
     * @param v
     * @return
     */
    public static String formatNum(float v) {
        return df.format(v);
    }

    /**
     * 格式化￥
     *
     * @param v
     * @return
     */
    public static String formatYuan(float v) {
        if (v <= 0) {
            return new DecimalFormat("￥###,###.##").format(0);
        }

        return new DecimalFormat("￥###,###.00").format(v);
    }

    /**
     * 格式化浮点数为整数
     *
     * @param v
     * @return 四舍五入法
     */
    public static int formatNum2Int(float v) {
        return Integer.valueOf(df_int.format(v));

    }

    /**
     * 格式化浮点数为整数
     *
     * @param v
     * @return 四舍五入法
     */
    public static int formatNum2Int(double v) {
        return Integer.valueOf(df_int.format(v));

    }

    /**
     * clamp value
     *
     * @param value 比较值
     * @param max   最大值
     * @param min   最小值
     * @return
     */
    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    /**
     * 随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    public static int getRndNum(int min, int max) {
        return (int) Math.round((max - min) * Math.random() + min);
    }

    /**
     * 随机数
     *
     * @param min      最小值
     * @param max      最大值
     * @param nomatchs
     * @return
     */
    public static int getRndNum(int min, int max, int[] nomatchs) {
        int i = getRndNum(min, max);
        if (nomatchs == null || nomatchs.length == 0)
            return i;
        int count = 0;
        while (true) {
            if (count > 5000) {
                return i;
            }
            boolean bEquals = false;
            for (int k = 0; k < nomatchs.length; k++) {
                if (nomatchs[k] == i) {
                    i = getRndNum(min, max);
                    bEquals = true;
                    break;
                }
            }
            if (!bEquals) {
                return i;
            }
            count++;
        }
    }

    /**
     * 浮点数截断
     *
     * @param f             被截断的浮点数
     * @param decimalPlaces 小数位数，必须大于0
     * @return 截断浮点数，采用四舍五入
     */
    public static float truncate(float f, int decimalPlaces) {
        float decimalShift = (float) Math.pow(10, decimalPlaces);
        return Math.round(f * decimalShift) / decimalShift;
    }

    /**
     * 四舍五入
     *
     * @param number  原数
     * @param decimal 保留几位小数
     * @return 四舍五入后的值
     */
    public static BigDecimal round(double number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 一维数组转为二维数组
     *
     * @param m
     * @param width
     * @param height
     * @return
     */
    public static int[][] arrayToMatrix(int[] m, int width, int height) {
        int[][] result = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int p = j * height + i;
                result[i][j] = m[p];
            }
        }

        return result;
    }

    /**
     * 二维数组转为一维数组
     *
     * @param m
     * @return
     */
    public static double[] matrixToArray(double[][] m) {
        int p = m.length * m[0].length;
        double[] result = new double[p];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                int q = j * m.length + i;
                result[q] = m[i][j];
            }
        }

        return result;
    }

    /**
     * int数组转换为double数组
     *
     * @param input
     * @return
     */
    public static double[] intToDoubleArray(int[] input) {
        int length = input.length;
        double[] output = new double[length];
        for (int i = 0; i < length; i++) {
            output[i] = Double.valueOf(String.valueOf(input[i]));
        }

        return output;
    }

    /**
     * int二维数组转换为double二维数组
     *
     * @param input
     * @return
     */
    public static double[][] intToDoubleMatrix(int[][] input) {
        int height = input.length;
        int width = input[0].length;
        double[][] output = new double[height][width];
        for (int i = 0; i < height; i++) {
            // 列
            for (int j = 0; j < width; j++) {
                // 行
                output[i][j] = Double.valueOf(String.valueOf(input[i][j]));
            }
        }

        return output;
    }

    /**
     * 计算数组的平均值
     *
     * @param pixels 数组
     * @return int 平均值
     */
    public static int average(int[] pixels) {
        float m = 0;
        for (int i = 0; i < pixels.length; ++i) {
            m += pixels[i];
        }
        m = m / pixels.length;

        return (int) m;
    }

    /**
     * 计算数组的平均值
     *
     * @param pixels 数组
     * @return int 平均值
     */
    public static int average(double[] pixels) {
        float m = 0;
        for (int i = 0; i < pixels.length; ++i) {
            m += pixels[i];
        }
        m = m / pixels.length;

        return (int) m;
    }

}
