package com.hw.hwdroid.foundation.utils.time;

import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 */
public class DateUtils {

    /**
     * 今天
     */
    public static Calendar getTodayCalendar() {
        Calendar cal_Today = Calendar.getInstance();
        cal_Today.set(Calendar.HOUR_OF_DAY, 0);
        cal_Today.set(Calendar.MINUTE, 0);
        cal_Today.set(Calendar.SECOND, 0);

        return cal_Today;
    }


    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatChinese(Date date, String pattern) {
        return format(date, pattern, Locale.CHINESE);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return format(date, pattern, null);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @param locale
     * @return
     */
    public static String format(Date date, String pattern, Locale locale) {
        try {
            SimpleDateFormat sdf = null == locale ? new SimpleDateFormat(pattern) :
                    new SimpleDateFormat(pattern, locale);
            return sdf.format(date);
        } catch (Exception e) {
            Logger.e(e);
        }

        return "";
    }

    /**
     * 指定日期格式，解析字符串日期为Date对象
     *
     * @param time
     * @param template
     * @return
     */
    public static Date parseDateChinese(String time, String template) {
        return parseDate(time, template, Locale.CHINESE);
    }

    /**
     * 指定日期格式，解析字符串日期为Date对象
     *
     * @param time
     * @param pattern
     * @return
     */
    public static Date parseDate(String time, String pattern) {
        return parseDate(time, pattern, null);
    }

    /**
     * 指定日期格式，解析字符串日期为Date对象
     *
     * @param time
     * @param pattern
     * @param locale  eg:Locale.CHINESE
     * @return
     */
    public static Date parseDate(String time, String pattern, Locale locale) {
        try {
            DateFormat inputDf = locale == null ? new SimpleDateFormat(pattern) :
                    new SimpleDateFormat(pattern, locale);
            return inputDf.parse(time);
        } catch (Exception e) { // 日期格式不合法
            Logger.e(e);
            return null;
        }
    }

    /**
     * 是否为同一天
     *
     * @param dayCalendar
     * @param dayCalendar2
     * @return
     */
    public static boolean isSameDay(Calendar dayCalendar, Calendar dayCalendar2) {
        if (null == dayCalendar || null == dayCalendar2) {
            return false;
        }

        // 同一天：年、月、日
        return dayCalendar.get(Calendar.DAY_OF_MONTH) == dayCalendar2.get(Calendar.DAY_OF_MONTH)
                && dayCalendar.get(Calendar.MONTH) == dayCalendar2.get(Calendar.MONTH)
                && dayCalendar.get(Calendar.YEAR) == dayCalendar2.get(Calendar.YEAR);
    }

    /**
     * 今天
     *
     * @param dayCalendar
     * @return
     */
    public static boolean isToday(Calendar dayCalendar) {
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTimeInMillis(System.currentTimeMillis());
        return isSameDay(dayCalendar, todayCalendar);
    }

    /**
     * 今天
     *
     * @param dayCalendar
     * @param todayCalendar
     * @return
     */
    public static boolean isToday(Calendar dayCalendar, Calendar todayCalendar) {
        if (null == todayCalendar) {
            todayCalendar = Calendar.getInstance();
            todayCalendar.setTimeInMillis(System.currentTimeMillis());
        }

        return isSameDay(dayCalendar, todayCalendar);
    }

    /**
     * 明天
     *
     * @param dayCalendar
     * @return
     */
    public static boolean isTomorrow(Calendar dayCalendar) {
        Calendar tomorrowCalendar = Calendar.getInstance();
        tomorrowCalendar.setTimeInMillis(System.currentTimeMillis());
        tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1);

        return isSameDay(dayCalendar, tomorrowCalendar);
    }

    /**
     * 明天
     *
     * @param dayCalendar
     * @param todayCalendar
     * @return
     */
    public static boolean isTomorrow(Calendar dayCalendar, Calendar todayCalendar) {
        Calendar afterTomorrowCalendar = Calendar.getInstance();

        if (null == todayCalendar) {
            todayCalendar = Calendar.getInstance();
            todayCalendar.setTimeInMillis(System.currentTimeMillis());
        }

        afterTomorrowCalendar.setTimeInMillis(todayCalendar.getTimeInMillis());
        afterTomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1);

        return isSameDay(dayCalendar, todayCalendar);
    }

    /**
     * 后天
     *
     * @param dayCalendar
     * @return
     */
    public static boolean isTheDayAfterTomorrow(Calendar dayCalendar) {
        Calendar afterTomorrowCalendar = Calendar.getInstance();
        afterTomorrowCalendar.setTimeInMillis(System.currentTimeMillis());
        afterTomorrowCalendar.add(Calendar.DAY_OF_MONTH, 2);

        return isSameDay(dayCalendar, afterTomorrowCalendar);
    }

    /**
     * 后天
     *
     * @param dayCalendar
     * @param todayCalendar
     * @return
     */
    public static boolean isTheDayAfterTomorrow(Calendar dayCalendar, Calendar todayCalendar) {
        Calendar afterTomorrowCalendar = Calendar.getInstance();

        if (null == todayCalendar) {
            todayCalendar = Calendar.getInstance();
            todayCalendar.setTimeInMillis(System.currentTimeMillis());
        }

        afterTomorrowCalendar.setTimeInMillis(todayCalendar.getTimeInMillis());
        afterTomorrowCalendar.add(Calendar.DAY_OF_MONTH, 2);

        return isSameDay(dayCalendar, todayCalendar);
    }

}
