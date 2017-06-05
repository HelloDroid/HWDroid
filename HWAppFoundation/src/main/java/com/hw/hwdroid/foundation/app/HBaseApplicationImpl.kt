package com.hw.hwdroid.foundation.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.view.Gravity
import android.widget.Toast
import com.hw.hwdroid.foundation.BuildConfig
import com.hw.hwdroid.foundation.utils.toast.ToastUtils
import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger
import common.android.foundation.app.HActivityLifecycleHelper


/**
 * supper Application
 *
 * Created by chen.jiana on 2015/9/28.
 */
open class HBaseApplicationImpl : Application() {

    /**
     * @see android.content.ContextWrapper.getApplicationContext
     */
    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        FoundationContext.inits(this)
        Logger.init().logLevel(if (BuildConfig.DEBUG) LogLevel.FULL else LogLevel.NONE)

        registerActivityLifecycleCallbacks(HActivityLifecycleHelper)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    /**
     * @see android.content.ContextWrapper.getPackageName
     */
    override fun getPackageName(): String {
        return super.getPackageName()
    }

    override fun onTerminate() {
        super.onTerminate()

        Logger.d("onTerminate...")
    }

    override fun onLowMemory() {
        super.onLowMemory()

        Logger.d("onLowMemory...")
    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        ToastUtils.show(applicationContext, message, Gravity.CENTER, 0, 0, duration)
    }

}
