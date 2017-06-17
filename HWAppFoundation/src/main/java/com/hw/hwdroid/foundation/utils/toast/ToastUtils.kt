package com.hw.hwdroid.foundation.utils.toast

import android.content.Context
import android.support.annotation.IntDef
import android.support.annotation.StringRes
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.hw.hwdroid.foundation.R
import com.hw.hwdroid.foundation.app.HWFoundationContext
import com.hw.hwdroid.foundation.utils.ResourceUtils
import com.hw.hwdroid.foundation.utils.StringUtils
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.concurrent.TimeUnit

/**
 * 自定义Toast Util
 *
 * @author chenJ
 * @date 2014-1-15
 */

class ToastUtils private constructor(_Context: Context) {

    private val customToast: CustomToast = CustomToast(_Context)

    /**
     * 自定义toast
     *
     * @param messageResId
     * @param duration
     * @param judgeAppIsForeground  判断APP是否在前台
     * @param action                延迟执行的动作
     */
    @Synchronized fun showCustomToast(@StringRes messageResId: Int, @ToastUtils.Duration duration: Int = Toast.LENGTH_SHORT, judgeAppIsForeground: Boolean = true, gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0, action: Consumer<Int>?) {
        customToast.show(messageResId, duration, judgeAppIsForeground, gravity, xOffset, yOffset, action)
    }

    /**
     * 自定义toast
     *
     * @param message
     * @param duration
     * @param judgeAppIsForeground  判断APP是否在前台
     * @param action                延迟执行的动作
     */
    @Synchronized fun showCustomToast(message: CharSequence, @ToastUtils.Duration duration: Int = Toast.LENGTH_SHORT, judgeAppIsForeground: Boolean = true, gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0, action: Consumer<Int>?) {
        customToast.show(message, duration, judgeAppIsForeground, gravity, xOffset, yOffset, action)
    }

    @IntDef(Toast.LENGTH_SHORT.toLong(), Toast.LENGTH_LONG.toLong())
    @Retention(RetentionPolicy.SOURCE)
    annotation class Duration

    companion object {

        private var toastUtil: ToastUtils? = null

        @Synchronized fun instance(): ToastUtils? {
            synchronized(ToastUtils::class.java) {
                if (toastUtil == null) {
                    val context = HWFoundationContext.getApplicationContext()
                    if (context != null) {
                        toastUtil = ToastUtils(context)
                    }
                }
            }

            return toastUtil
        }

        @Synchronized fun releaseToast() {
            toastUtil = null
        }

        /**
         *
         */
        @JvmOverloads fun show(context: Context, @StringRes messageResId: Int, @Duration duration: Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0, action: Consumer<Int>? = null) {
            show(context, ResourceUtils.getString(context, messageResId, ""), duration, gravity, xOffset, yOffset, action)
        }

        /**
         * 显示冒泡提示
         *
         * @param context
         * @param message
         * @param gravity       Gravity.CENTER
         * @param xOffset       0
         * @param yOffset       0
         * @param duration           duration
         * @param action        延迟执行的动作
         */
        @JvmOverloads fun show(context: Context, message: CharSequence, @Duration duration: Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0, action: Consumer<Int>? = null) {
            if (StringUtils.isNullOrWhiteSpace(message)) {
                return
            }

            val toast = Toast(context)
            val view = LayoutInflater.from(context.applicationContext).inflate(R.layout.ab__toast_layout, null)
            (view.findViewById(R.id.my_toast) as? TextView)?.text = message
            toast.view = view

            toast.setGravity(gravity, xOffset, yOffset)
            toast.duration = duration
            toast.show()

            if (action != null) {
                Observable.just(1).delay((duration + 1).toLong(), TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(action, Consumer { onError -> Logger.e(onError) })
            }
        }
    }

}