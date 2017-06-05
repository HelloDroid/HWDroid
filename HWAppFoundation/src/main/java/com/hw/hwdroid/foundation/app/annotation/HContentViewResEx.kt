package com.hw.hwdroid.foundation.app.annotation

import android.support.annotation.LayoutRes
import android.view.View
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * MyBaseAdapter Item View Resource
 *
 * Created by ChenJ on 16/8/9.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class HContentViewResEx(
        @LayoutRes val groupResId: Int = View.NO_ID,
        @LayoutRes val childResId: Int = View.NO_ID
)


//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface HContentViewResEx {
//
//    @LayoutRes int groupResId() default View.NO_ID;
//
//    @LayoutRes int childResId() default View.NO_ID;
//
//}