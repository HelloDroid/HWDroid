package com.hw.hwdroid.foundation.utils.task;

import com.orhanobut.logger.Logger;

/**
 */
public class PriorityRunnable implements Runnable {

    public final Priority priority;
    private final Runnable runnable;
    private long createTime;

    public PriorityRunnable(Runnable runnable) {
        this(null, runnable);
    }

    public PriorityRunnable(Priority priority, Runnable runnable) {
        this.priority = priority == null ? Priority.DEFAULT : priority;
        this.runnable = runnable;

        this.createTime = System.currentTimeMillis();
    }

    @Override
    public final void run() {
        long waitTime = System.currentTimeMillis() - createTime;
        if (waitTime > 2000) {
            Logger.e(new RuntimeException("waiting for a long time " + waitTime));
            // throw new RuntimeException("waiting for a long time " + waitTime);
        }

        this.runnable.run();
    }

}
