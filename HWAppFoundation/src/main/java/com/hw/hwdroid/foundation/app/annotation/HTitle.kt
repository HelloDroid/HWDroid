package com.hw.hwdroid.foundation.app.annotation

import android.support.annotation.StringRes
import android.view.View
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Theme Light
 * Created by ChenJ on 2017/2/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class HTitle(@StringRes val value: Int = View.NO_ID)
