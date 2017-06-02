package com.hw.hwdroid.foundation.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 单位转换工具类
 */
public class UnitConverterUtils {

    private static float converterValue(float value) {
        return 0.5f * (value >= 0 ? 1 : -1);
    }

    /**
     * dip转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + converterValue(dipValue));
    }

    /**
     * px转dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f * (pxValue >= 0 ? 1 : -1));
    }

    /**
     * sp转px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f * (spValue >= 0 ? 1 : -1));
    }

    /**
     * 密度
     */
    public static float getDensity(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * Dip转换为实际屏幕的像素值
     *
     * @param dm
     * @param dip
     * @return
     */
    public static int getPixelFromDip(@NonNull DisplayMetrics dm, float dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm) + converterValue(dip));
    }


    /**
     * Dip转换为实际屏幕的像素值
     *
     * @param context
     * @param dip
     * @return
     */
    public static int getPixelFromDip(@NonNull Context context, float dip) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm) + converterValue(dip));
    }

    /**
     * Dip转换为实际屏幕的像素值
     *
     * @param res
     * @param dip
     * @return
     */
    public static int getPixelFromDip(@NonNull Resources res, float dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, res.getDisplayMetrics()) + converterValue(dip));
    }

}
