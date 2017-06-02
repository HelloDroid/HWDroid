package com.hw.hwdroid.foundation.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

public class SpannableUtils {
    /**
     * 设置字体大小，用sp
     *
     * @param context
     * @param str     目标字符串
     * @param start   开始位置
     * @param end     结束位置
     * @param spSize  sp大小
     * @return
     */
    public static SpannableString getSizeSpanUseSp(Context context, String str, int start, int end, int spSize) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new AbsoluteSizeSpan(UnitConverterUtils.sp2px(context, spSize)), start, end,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置前景色
     *
     * @param context
     * @param str     目标字符串
     * @param start   开始位置
     * @param end     结束位置
     * @param color   颜色值 如Color.BLACK
     * @return
     */
    public static SpannableString getForegroundColorSpan(Context context, String str, int start, int end, int color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
