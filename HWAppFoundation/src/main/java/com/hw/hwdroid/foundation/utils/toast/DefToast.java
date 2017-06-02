/**
 * @(#)DefToast.java 2014-1-15 Copyright 2014 . All rights
 * reserved.
 */

package com.hw.hwdroid.foundation.utils.toast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Toast
 * <p/>
 * <pre>
 * Note:使用该方法在连续点击 某一操作显示toast时，不能达到控制的效果,不是单例模式
 * </pre>
 *
 * @author chenj
 * @date 2014-1-15
 */

public final class DefToast {

    /**
     * 显示短时间LENGTH_SHORT的toast
     *
     * @param context
     * @param msg
     */
    public static void showShort(@NonNull Context context, @NonNull final String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时间的toast
     *
     * @param context
     * @param resouceId text resouce id
     */
    public static void showShort(@NonNull Context context, @StringRes final int resouceId) {
        show(context, resouceId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示长时间的toast
     *
     * @param context
     * @param msg     The text to show
     */
    public static void showLong(@NonNull Context context, @NonNull final String msg) {
        show(context, msg, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时间的toast
     *
     * @param context
     * @param resouceId text resouce id
     */
    public static void showLong(@NonNull Context context, @StringRes final int resouceId) {
        show(context, resouceId, Toast.LENGTH_LONG);
    }

    /**
     * @param context
     * @param textResId text resouce id
     * @param duration  显示时长
     */
    private static void show(@NonNull Context context, @StringRes final int textResId, final int duration) {
        final String text = context.getString(textResId);
        if (null == text || text.length() == 0) {
            return;
        }

        show(context, text, duration);
    }

    /**
     * show toast
     *
     * @param context
     * @param text     The text to show
     * @param duration 显示时长.  Either {@link Toast#LENGTH_SHORT} or  {@link Toast#LENGTH_LONG}
     */
    public static void show(@NonNull Context context, @NonNull final String text, final int duration) {
        Toast.makeText(context, text, duration).show();
    }

    /**
     * show toast
     *
     * @param context
     * @param text
     * @param duration
     * @param gravity  Gravity.CENTER
     * @param xOffset
     * @param yOffset
     */
    public static void show(@NonNull Context context, @NonNull final String text, final int duration, int gravity, int xOffset, int yOffset) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }


    /**
     * show toast
     *
     * @param context
     * @param text
     * @param duration
     * @param gravity  Gravity.CENTER
     */
    public static void show(@NonNull Context context, @NonNull final String text, final int duration, int gravity) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

}
