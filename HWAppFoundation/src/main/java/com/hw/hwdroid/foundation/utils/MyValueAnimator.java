package com.hw.hwdroid.foundation.utils;

import android.content.Context;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by ChenJ on 16/7/5.
 */
public class MyValueAnimator {

    public static ValueAnimator ofFloat(long duration, int repeatCount, boolean startAnimator, ValueAnimator.AnimatorUpdateListener listener, Animator.AnimatorListener animatorListener, float... values) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(values);
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatCount(repeatCount);

        if (null != listener) {
            valueAnimator.addUpdateListener(listener);
        }

        if (null != animatorListener) {
            valueAnimator.addListener(animatorListener);
        }

        if (startAnimator) {
            valueAnimator.start();
        }

        return valueAnimator;
    }

    public static ValueAnimator ofFloat(long duration, int repeatCount, boolean startAnimator, ValueAnimator.AnimatorUpdateListener listener, float... values) {
        return ofFloat(duration, repeatCount, startAnimator, listener, null, values);
    }

    /**
     * 播放ValueAnimator
     *
     * @param context
     * @param repeatCount
     * @param listener
     * @param values
     */
    public static void startValueAnimatorShortTime(Context context, int repeatCount, ValueAnimator.AnimatorUpdateListener listener, float... values) {
        ofFloat(context.getResources().getInteger(android.R.integer.config_shortAnimTime), repeatCount, true, listener, null, values);
    }

    /**
     * 播放ValueAnimator
     *
     * @param context
     * @param repeatCount
     * @param listener
     * @param values
     */
    public static void startValueAnimatorMediumTime(Context context, int repeatCount, ValueAnimator.AnimatorUpdateListener listener, float... values) {
        ofFloat(context.getResources().getInteger(android.R.integer.config_mediumAnimTime), repeatCount, true, listener, null, values);
    }

    /**
     * 播放ValueAnimator
     *
     * @param context
     * @param repeatCount
     * @param listener
     * @param animatorListener
     * @param values
     */
    public static void startValueAnimatorMediumTime(Context context, int repeatCount, ValueAnimator.AnimatorUpdateListener listener, Animator.AnimatorListener animatorListener, float... values) {
        ofFloat(context.getResources().getInteger(android.R.integer.config_mediumAnimTime), repeatCount, true, listener, animatorListener, values);
    }

    /**
     * 播放ValueAnimator
     *
     * @param context
     * @param repeatCount
     * @param listener
     * @param values
     */
    public static void startValueAnimatorLongTime(Context context, int repeatCount, ValueAnimator.AnimatorUpdateListener listener, float... values) {
        ofFloat(context.getResources().getInteger(android.R.integer.config_longAnimTime), repeatCount, true, listener, null, values);
    }

}
