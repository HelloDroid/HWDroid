package com.hw.hwdroid.foundation.app

import android.app.Activity
import android.content.Context
import common.android.foundation.app.HWActivityStack


/**
 * Foundation
 *
 *
 * Created by ChenJ on 2017/2/16.
 */

object HWFoundationContext {

    // TitleBar 默认使用IOS模式
    @JvmStatic var useIosModeForTitleBar = false

    var context: Context? = null

    @JvmStatic fun inits(context: Context) {
        this.context = context
    }

    @JvmStatic fun currActivity(): Activity? {
        return HWActivityStack.curr()
    }

    @JvmStatic fun context(): Context? {
        return context ?: currActivity()
    }

    @JvmStatic fun getApplicationContext(): Context? {
        context?.let {
            return context?.applicationContext
        }

        return currActivity()?.applicationContext
    }

}
