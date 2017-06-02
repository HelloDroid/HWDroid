package com.hw.hwdroid.foundation.app.annotation;

import android.support.annotation.LayoutRes;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**

 */

/**
 * MyBaseAdapter Item View Resource
 * <p>
 * Created by ChenJ on 16/8/9.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HContentViewResEx {

    @LayoutRes int groupResId() default View.NO_ID;

    @LayoutRes int childResId() default View.NO_ID;

}
