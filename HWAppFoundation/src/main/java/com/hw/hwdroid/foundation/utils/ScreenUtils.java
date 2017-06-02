package com.hw.hwdroid.foundation.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.provider.Settings;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 获得屏幕相关的辅助类
 * <p>
 * Created by chenjian on 16/1/21.
 */
public class ScreenUtils {

    private ScreenUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @return
     * @Description
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        return dm.heightPixels;
    }

    /**
     * 获取屏幕宽高
     *
     * @return
     * @Description
     */
    public static int[] getScreenWH(Context context) {
        int[] wh = new int[2];

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        wh[0] = dm.widthPixels;
        wh[1] = dm.heightPixels;

        // int orientation =
        // KTruetouchApplication.mOurApplication.getResources().getConfiguration().orientation;
        // if (orientation == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
        // } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
        // }

        return wh;
    }

    /**
     * 密度
     *
     * @param context
     * @return
     */
    public static int getDensityDpi(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        return dm.densityDpi;
    }

    /**
     * 终端设备最短边
     *
     * @param context
     * @return 7英寸最短边为600dp
     */
    public static int getSmallestScreenWidthDp(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return config.smallestScreenWidthDp;
    }

    public static float computeScale4DensityDpi(Context context, int dpi) {
        return ((float) context.getResources().getDisplayMetrics().densityDpi) / dpi;
    }

    /**
     * 状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return statusBarHeight;
    }

    /**
     * Default status dp = 24 or 25
     * mhdpi = dp * 1
     * hdpi = dp * 1.5
     * xhdpi = dp * 2
     * xxhdpi = dp * 3
     * eg : 1920x1080, xxhdpi, => status/all = 25/640(dp) = 75/1080(px)
     * <p>
     * don't forget toolbar's dp = 48
     *
     * @return px
     */
    @IntRange(from = 0, to = 75)
    public static int getStatusBarOffsetPx(Context context) {
        Context appContext = context.getApplicationContext();
        int result = 0;
        int resourceId = appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarFromRectTop(@NonNull Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }


    /**
     * 是否自动旋转
     *
     * @param context
     * @return
     */
    public static boolean isAutoRotate(Context context) {
        if (context == null)
            return false;

        return Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
    }

    /**
     * 检测屏幕方向
     *
     * @param context
     * @return
     */
    public static int getScreenOrientation(Context context) {
        Configuration conf = context.getResources().getConfiguration();
        return conf.orientation;
    }

    /**
     * 是否是横屏
     *
     * @param context
     * @return
     */
    public static boolean isOrientationLandscape(Context context) {
        Configuration conf = context.getResources().getConfiguration();
        if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 屏幕方向
     *
     * @param context
     * @return
     */
    public static int getRotation(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        int rotate = 0;
        switch (d.getRotation()) {
            case Surface.ROTATION_0: // 手机处于正常状态
                rotate = 0;
                break;

            case Surface.ROTATION_90:// 手机旋转90度
                rotate = 1;
                break;

            case Surface.ROTATION_180:// 手机旋转180度
                rotate = 2;
                break;

            case Surface.ROTATION_270:// 手机旋转270度
                rotate = 3;
                break;

            default:
                break;
        }

        return rotate;
    }

    /**
     * 屏幕方向角度
     *
     * @param context
     * @return
     */
    public static int getRotationAngle(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        int angle = 0;
        switch (d.getRotation()) {
            case Surface.ROTATION_0: // 手机处于正常状态
                angle = 0;
                break;

            case Surface.ROTATION_90:// 手机旋转90度
                angle = 90;
                break;

            case Surface.ROTATION_180:// 手机旋转180度
                angle = 180;
                break;

            case Surface.ROTATION_270:// 手机旋转270度
                angle = 270;
                break;

            default:
                break;
        }

        return angle;
    }

    /**
     * 屏幕Rect
     *
     * @param context
     * @return Rect
     */
    public Rect getScreenBoundRect(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Rect rect = new Rect(0, 0, dm.widthPixels, dm.heightPixels);

        return rect;
    }

    /**
     * 设备是否处于锁屏状态
     *
     * @param context
     * @return
     */
    public static boolean isScreenLocked(Context context) {
        if (context == null)
            return true;

        KeyguardManager mKeyguardManager = (KeyguardManager)
                context.getSystemService(Context.KEYGUARD_SERVICE);

        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

}
