package com.hw.hwdroid.foundation.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加载数据
 * <p>
 * Created by ChenJ on 2017/2/16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HLoadService {
    boolean value() default true;
}
