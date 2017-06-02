package com.hw.hwdroid.foundation.app;

import android.support.annotation.StringRes;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ChenJ on 2017/2/16.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HTitle {
    @StringRes int value() default View.NO_ID;
}
