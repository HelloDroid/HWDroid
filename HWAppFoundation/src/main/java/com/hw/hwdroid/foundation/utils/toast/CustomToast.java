package com.hw.hwdroid.foundation.utils.toast;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.hwdroid.foundation.R;
import com.hw.hwdroid.foundation.utils.AppUtils;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


/**
 * 自定义冒泡提示
 *
 * @author chenjian
 * @date 2014-1-15
 */

public class CustomToast {


    private Toast toast;
    private TextView textView;

    private Context context;
    private Handler mHandler;

    /**
     * CustomToast构造函数
     *
     * @param context
     */
    public CustomToast(@NonNull Context context) {
        this(context, Gravity.CENTER, 0, 0);
    }

    /**
     * CustomToast构造函数
     *
     * @param context
     * @param gravity Gravity.CENTER
     * @param xOffset
     * @param yOffset
     */
    public CustomToast(@NonNull Context context, int gravity, int xOffset, int yOffset) {
        this.context = context;

        mHandler = new Handler();
        toast = new Toast(context);

        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.ab__toast_layout, null);

        textView = (TextView) view.findViewById(R.id.my_toast);
        toast.setView(view);

        toast.setGravity(gravity, xOffset, yOffset);

        /*
        if (gravity == Gravity.TOP) {
            toast.setGravity(Gravity.TOP, 0, 0);
        }
        if (gravity == Gravity.BOTTOM) {
            toast.setGravity(Gravity.BOTTOM, 0, 0);
        } else {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        */
    }

    /**
     * 显示短时间冒泡提示
     *
     * @param msgResId
     */
    public void showShort(@StringRes final int msgResId) {
        show(msgResId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时间冒泡提示
     *
     * @param text
     */
    public void showShort(@NonNull final String text) {
        show(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示长时间冒泡提示
     *
     * @param msgResId
     */
    public void showLong(@StringRes final int msgResId) {
        show(msgResId, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时间冒泡提示
     *
     * @param text
     */
    public void showLong(@NonNull final String text) {
        show(text, Toast.LENGTH_LONG);
    }

    /**
     * 显示冒泡提示
     *
     * @param msgResId
     * @param duration
     */
    public void show(@StringRes final int msgResId, final int duration) {
        String msg = this.context.getString(msgResId);
        show(msg, duration);
    }

    /**
     * 显示冒泡提示
     *
     * @param msg
     * @param duration
     */
    public void show(@NonNull final String msg, final int duration) {
        show(msg, duration, false);
    }

    /**
     * 显示冒泡提示
     *
     * @param msg
     * @param duration
     * @param judgeAppIsForeground 判断APP是否在前台
     */
    public synchronized void show(@NonNull final String msg, final int duration, final boolean judgeAppIsForeground) {
        show(msg, duration, judgeAppIsForeground, null);
    }

    /**
     * 显示冒泡提示
     *
     * @param msg
     * @param duration
     * @param judgeAppIsForeground 判断APP是否在前台
     * @param action               延迟执行动作
     */
    public synchronized void show(@NonNull final String msg, final int duration, final boolean judgeAppIsForeground, Consumer<Integer> action) {
        if (null == textView) {
            return;
        }

        mHandler.post(() -> {
            // 不在前台不需要提示
            if (judgeAppIsForeground && !AppUtils.appIsForeground(context)) {
                return;
            }

            if (duration == Toast.LENGTH_LONG) {
                toast.setDuration(Toast.LENGTH_LONG);
            } else {
                toast.setDuration(Toast.LENGTH_SHORT);
            }

            textView.setText(msg);
            toast.show();
        });

        if (null != action) {
            Observable.just(1).delay(duration + 1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(action, onErr -> Logger.e(onErr));
        }
    }

}
