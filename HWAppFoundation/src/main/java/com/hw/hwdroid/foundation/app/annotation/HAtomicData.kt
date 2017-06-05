package com.hw.hwdroid.foundation.app.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * 使用原子数据
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class HAtomicData(val value: Boolean = false)


//
//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface HAtomicData {
//        /** 使用原子数据 */
//        boolean value() default false;
//}
