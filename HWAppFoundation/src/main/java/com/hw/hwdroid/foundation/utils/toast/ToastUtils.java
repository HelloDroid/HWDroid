package com.hw.hwdroid.foundation.utils.toast;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.hwdroid.foundation.R;
import com.hw.hwdroid.foundation.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 自定义Toast Util
 *
 * @author chenjian
 * @date 2014-1-15
 */

public class ToastUtils {

    private CustomToast customToast;

    private static ToastUtils toastUtil;

    private ToastUtils(Context _Context) {
        customToast = new CustomToast(_Context);
    }

    public synchronized static ToastUtils instance(Context _Context) {
        synchronized (ToastUtils.class) {
            if (toastUtil == null) {
                toastUtil = new ToastUtils(_Context);
            }
        }

        return toastUtil;
    }

    public synchronized static void releaseToast() {
        toastUtil = null;
    }

    /**
     * 自定义短时间toast
     *
     * @param msgResId
     */
    public synchronized void showCustomToastShort(@StringRes final int msgResId) {
        customToast.showShort(msgResId);
    }

    /**
     * 自定义短时间toast
     *
     * @param text
     */
    public synchronized void showCustomToastShort(@NonNull final String text) {
        customToast.showShort(text);
    }

    /**
     * 自定义长时间toast
     *
     * @param msgResId
     */
    public synchronized void showCustomToastLong(@StringRes final int msgResId) {
        customToast.showLong(msgResId);
    }

    /**
     * 自定义长时间toast
     *
     * @param text
     */
    public synchronized void showCustomToastLong(@NonNull final String text) {
        customToast.showLong(text);
    }

    /**
     * 自定义toast
     *
     * @param msgResId
     * @param duration
     */
    public synchronized void showCustomToast(@StringRes final int msgResId, final int duration) {
        customToast.show(msgResId, duration);
    }

    public synchronized void showCustomToast(@NonNull final String msg, final int duration) {
        customToast.show(msg, duration);
    }

    /**
     * 自定义toas
     *
     * @param msg
     * @param duration
     * @param judgeAppIsForeground 判断APP是否在前台
     */
    public synchronized void showCustomToast(@NonNull final String msg, final int duration, final boolean judgeAppIsForeground) {
        customToast.show(msg, duration, judgeAppIsForeground);
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msg
     */
    public static void show(@NonNull Context context, @NonNull String msg) {
        show(context, msg, Gravity.CENTER, 0, 0, Toast.LENGTH_SHORT);
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msg
     * @param action  延迟执行的动作
     */
    public static void show(@NonNull Context context, @NonNull String msg, Consumer<Integer> action) {
        show(context, msg, Gravity.CENTER, 0, 0, Toast.LENGTH_SHORT, action);
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msg
     * @param defMsgResId msg为空时使用defMsgResId
     */
    public static void show2(@NonNull Context context, @NonNull String msg, @StringRes final int defMsgResId) {
        if (StringUtils.isNullOrWhiteSpace(msg)) {
            show(context, defMsgResId);
        } else {
            show(context, msg, Gravity.CENTER, 0, 0, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msgResId
     * @param action   延迟执行的动作
     */
    public static void show(@NonNull Context context, @StringRes final int msgResId, Consumer<Integer> action) {
        String msg = context.getString(msgResId);
        show(context, msg, Gravity.CENTER, 0, 0, Toast.LENGTH_SHORT, action);
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msgResId
     */
    public static void show(@NonNull Context context, @StringRes final int msgResId) {
        show(context, msgResId, null);
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msg
     */
    public static void showLong(@NonNull Context context, String msg) {
        show(context, msg, Gravity.CENTER, 0, 0, Toast.LENGTH_LONG);
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msgResId
     */
    public static void showLong(@NonNull Context context, @StringRes final int msgResId) {
        String msg = context.getString(msgResId);
        show(context, msg, Gravity.CENTER, 0, 0, Toast.LENGTH_LONG);
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msgResId
     * @param duration
     */
    public static void show(@NonNull Context context, @StringRes final int msgResId, int duration) {
        show(context, msgResId, duration, null);
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msgResId
     * @param duration
     * @param action   延迟执行的动作
     */
    public static void show(@NonNull Context context, @StringRes final int msgResId, int duration, Consumer<Integer> action) {
        String msg = context.getString(msgResId);
        show(context, msg, Gravity.CENTER, 0, 0, duration, action);
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msg
     * @param duration
     */
    public static void show(@NonNull Context context, @NonNull String msg, int duration) {
        show(context, msg, Gravity.CENTER, 0, 0, duration);
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msg
     * @param gravity
     * @param duration
     */
    public static void show(@NonNull Context context, @NonNull String msg, int gravity, int duration) {
        show(context, msg, gravity, 0, 0, duration);
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msg
     * @param gravity  Gravity.CENTER
     * @param xOffset
     * @param yOffset
     * @param duration
     */
    public static void show(@NonNull Context context, @NonNull String msg, int gravity, int xOffset, int yOffset, int duration) {
        show(context, msg, gravity, xOffset, yOffset, duration, null);
    }

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    /**
     * 显示冒泡提示
     *
     * @param context
     * @param msg
     * @param gravity  Gravity.CENTER
     * @param xOffset
     * @param yOffset
     * @param duration
     * @param action   延迟执行的动作
     */
    public static void show(@NonNull Context context,
                            @NonNull String msg, int gravity, int xOffset, int yOffset, @Duration int duration, Consumer<Integer> action) {
        if (null == context || StringUtils.isNullOrWhiteSpace(msg)) {
            return;
        }

        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.ab__toast_layout, null);
        ((TextView) view.findViewById(R.id.my_toast)).setText(msg);
        toast.setView(view);

        toast.setGravity(gravity, xOffset, yOffset);

        toast.setDuration(duration);
        toast.show();

        if (null != action) {
            Observable.just(1).delay(duration + 1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(action, onError -> Logger.e(onError));
        }
    }

}
