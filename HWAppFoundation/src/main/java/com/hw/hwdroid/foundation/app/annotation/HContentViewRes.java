package com.hw.hwdroid.foundation.app.annotation;

import android.support.annotation.LayoutRes;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Activity ContentView Resource
 * <p>
 * Created by ChenJ on 16/8/4.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HContentViewRes {
    @LayoutRes int value() default View.NO_ID;
}
