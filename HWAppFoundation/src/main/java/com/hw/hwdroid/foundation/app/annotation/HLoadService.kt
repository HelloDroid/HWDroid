package com.hw.hwdroid.foundation.app.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * 加载数据
 * Created by ChenJ on 2017/2/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class HLoadService(val value: Boolean = true)


//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface HLoadService {
//    boolean value() default true;
//}
