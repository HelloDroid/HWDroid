package com.hw.hwdroid.foundation.app.annotation

import android.support.annotation.LayoutRes
import android.view.View
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * ContentView Resource
 *
 * Created by ChenJ on 16/8/4.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class HContentViewRes(
        @LayoutRes val value: Int = View.NO_ID,
        val titleBar: Boolean = true)
