package com.hw.hwdroid.foundation.app.rx.bus.annotation;

import android.support.annotation.NonNull;

import com.hw.hwdroid.foundation.app.rx.bus.event.HEventThread;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * Created by Android on 2016/6/8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HSubscribe {
    @NonNull int tag();

    HEventThread thread() default HEventThread.MAIN_THREAD;
}
