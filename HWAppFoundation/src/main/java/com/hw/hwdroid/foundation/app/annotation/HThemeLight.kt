package com.hw.hwdroid.foundation.app.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * title Resource
 * Created by ChenJ on 16/8/4.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class HThemeLight(val value: Boolean = true)
