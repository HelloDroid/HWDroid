package com.hw.hwdroid.foundation.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * 字体工具类
 */
public class FontsUtils {

    /**
     * 设置字体
     *
     * @param context
     * @param textView
     * @param font
     */
    public static void setTypeface(@NonNull Context context, @NonNull TextView textView, String font) {
        // 将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), font);
        // 应用字体
        textView.setTypeface(typeFace);
    }

}
