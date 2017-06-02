package com.hw.hwdroid.foundation.app.annotation;

import android.support.annotation.LayoutRes;
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
public @interface HActivityInit {

    @LayoutRes int value() default View.NO_ID;

    boolean HAddTitleBar() default false;

    // android:fitsSystemWindows="true"
    boolean HSetFitsSystemWindows() default true;
}
