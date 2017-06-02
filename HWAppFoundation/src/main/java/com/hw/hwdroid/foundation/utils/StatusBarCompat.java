package com.hw.hwdroid.foundation.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;

/**
 * Created by chenjian on 15/11/4.
 */
public class StatusBarCompat {
    public static final int INVALID_VAL = -1;
    public static final int COLOR_DEFAULT = Color.parseColor("#20000000");

    //    /**
    //     * 设置状态栏与Toolbar颜色一致
    //     *
    //     * @param activity
    //     */
    //    public static void setStatusBarPrimaryColor(@NonNull Activity activity) {
    //        setStatusBarPrimaryColor(activity, R.color.theme_default_primary);
    //    }

    /**
     * 设置状态栏与Toolbar颜色一致
     *
     * @param activity
     * @param colorResId
     */
    public static void setStatusBarPrimaryColor(@NonNull Activity activity, @ColorRes int colorResId) {
        if (null == activity) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }


        setTranslucentStatus(activity, true);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(colorResId);//通知栏所需颜色
    }

    @TargetApi(19)
    private static void setTranslucentStatus(@NonNull Activity activity, boolean on) {
        if (null == activity) {
            return;
        }

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void compat(Activity activity, int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                activity.getWindow().setStatusBarColor(statusColor);
            }

            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }

            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }
    }

    public static void compat(Activity activity) {
        compat(activity, INVALID_VAL);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    /**
     * status bar
     *
     * @param context
     * @param show
     */
    public static void showStatusBar(Context context, boolean show) {
        if (!show) {
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 14) {
            // android 4.0及以上
            setSystemUiVisibility(((Activity) context).getWindow().getDecorView(), show ? 0 : 2);
            setSystemUiVisibility(((Activity) context).getWindow().getDecorView(), show ? 0 : 1);
        } else if (Build.VERSION.SDK_INT >= 11) {
            // android 3.0及以上
            setSystemUiVisibility(((Activity) context).getWindow().getDecorView(), show ? 0 : 1);
        }
    }

    /**
     * @param view
     * @param visibility
     */
    private static void setSystemUiVisibility(View view, int visibility) {
        try {
            Class<View> clazz = View.class;
            Method method = clazz.getDeclaredMethod("setSystemUiVisibility", int.class);
            method.setAccessible(true);
            method.invoke(view, visibility);
        } catch (Exception e) {
            Logger.e(e);
        }
    }


}
