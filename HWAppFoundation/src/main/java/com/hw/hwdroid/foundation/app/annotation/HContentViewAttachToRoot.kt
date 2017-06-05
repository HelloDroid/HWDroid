package com.hw.hwdroid.foundation.app.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class HContentViewAttachToRoot(val value: Boolean = false)


//
//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface HContentViewAttachToRoot {
//
//    boolean value() default false;
//
//}
