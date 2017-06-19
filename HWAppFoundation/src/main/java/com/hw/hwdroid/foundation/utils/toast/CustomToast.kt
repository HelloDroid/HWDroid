package com.hw.hwdroid.foundation.utils.toast

import android.content.Context
import android.os.Handler
import android.support.annotation.StringRes
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.hw.hwdroid.foundation.R
import com.hw.hwdroid.foundation.utils.AppUtils
import com.hw.hwdroid.foundation.utils.ResourceUtils
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit


/**
 * 自定义冒泡提示
 *
 * @author chenJ
 * @date 2014-1-15
 */

class CustomToast constructor(private val context: Context) {

    private val textView: TextView?
    private val toast: Toast = Toast(context)
    private val handler: Handler = Handler()

    init {
        val view = LayoutInflater.from(context.applicationContext).inflate(R.layout.ab__toast_layout, null)
        textView = view.findViewById(R.id.my_toast) as? TextView
        toast.view = view
    }

    @Synchronized fun show(@StringRes messageResId: Int, @ToastUtils.Duration dur: Int = Toast.LENGTH_SHORT, judgeAppIsForeground: Boolean = true, gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0, action: Consumer<Int>?) {
        show(ResourceUtils.getString(context, messageResId, ""), dur, judgeAppIsForeground, gravity, xOffset, yOffset, action)
    }

    /**
     * 显示冒泡提示
     *
     * @param message
     * @param dur
     * @param judgeAppIsForeground  判断APP是否在前台
     * @param action                延迟执行动作
     */
    @Synchronized fun show(message: CharSequence?, @ToastUtils.Duration dur: Int = Toast.LENGTH_SHORT, judgeAppIsForeground: Boolean = true, gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0, action: Consumer<Int>?) {
        // 不在前台不需要提示
        if (message.isNullOrBlank() || judgeAppIsForeground && !AppUtils.appIsForeground(context)) {
            return
        }

        handler.post {
            textView?.text = message

            toast.setGravity(gravity, xOffset, yOffset)
            toast.duration = dur
            toast.show()
        }

        if (null != action) {
            Observable.just(1).delay((dur + 1).toLong(), TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(action, Consumer { onErr -> Logger.e(onErr) })
        }
    }

}