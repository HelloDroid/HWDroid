package com.hw.hwdroid.foundation.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.hw.hwdroid.foundation.BuildConfig
import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger
import common.android.foundation.app.HWActivityLifecycleHelper


/**
 * supper Application
 *
 * Created by chen.jiana on 2015/9/28.
 */
open class HWBaseApplicationImpl : Application() {

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

        HWFoundationContext.inits(this)
        Logger.init().logLevel(if (BuildConfig.DEBUG) LogLevel.FULL else LogLevel.NONE)

        registerActivityLifecycleCallbacks(HWActivityLifecycleHelper)
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
        Logger.e("onLowMemory...")
    }

}
