/**
 * @(#)DataItem.java 2014-3-7 Copyright 2014 . All rights
 * reserved.
 */

package com.hw.hwdroid.foundation.utils.time;

import com.hw.hwdroid.foundation.utils.HashCodeHelper;
import com.hw.hwdroid.foundation.utils.NumberUtils;
import com.hw.hwdroid.foundation.utils.StringUtils;

/**
 * Date bean
 *
 * @author chenj
 * @date 2014-3-7
 */

public class Datebean implements Comparable<Datebean> {

    // 年
    public String year;

    // 周
    public String week;

    // 日期(07-31)
    public String dateTime;

    /**
     * 返回毫秒类型时间
     *
     * @return
     */
    public long getMilitime() {
        return TimeUtils.parseDate(year + "-" + dateTime, TimeUtils.TIMEFORMAT_YMD, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return super.equals(o);

        if (!StringUtils.equals(((Datebean) o).year, year))
            return false;

        return StringUtils.equals(((Datebean) o).dateTime, this.dateTime);

        // return super.equals(o);
    }

    @Override
    public int hashCode() {
        try {
            HashCodeHelper hashCodeHelper = HashCodeHelper.getInstance();
            hashCodeHelper.appendInt(NumberUtils.parseInt(year, 0)).appendInt(NumberUtils.parseInt(week, 0)).appendObj(dateTime);

            return hashCodeHelper.getHashCode();
        } catch (Exception e) {
        }

        return super.hashCode();
    }

    @Override
    public int compareTo(Datebean another) {
        try {
            if (another == null) {
                return 0;
            }

            if (getMilitime() > another.getMilitime()) {
                return 1;
            }

            if (getMilitime() < another.getMilitime()) {
                return -1;
            }

        } catch (Exception e) {
        }

        return 0;
    }

}
