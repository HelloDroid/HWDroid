package common.android.foundation.app

import android.app.Activity
import android.app.Application
import android.os.Bundle

import com.orhanobut.logger.Logger

/**
 * Activity Lifecycle Helper
 *
 *
 * Created by ChenJ on 2017/5/11.
 */
object HWActivityLifecycleHelper : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
        Logger.d(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        Logger.d(activity)
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        Logger.d(activity)
    }

}
