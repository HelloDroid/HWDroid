package com.hw.hwdroid.foundation.app.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class HFitsSystemWindows(val value: Boolean = true)
