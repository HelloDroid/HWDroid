package com.hw.hwdroid.foundation.utils.task;

import com.orhanobut.logger.Logger;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池管理类
 * 支持优先级
 */
public class PriorityExecutor implements Executor {
    /**
     * 线程池维持的最小线程量
     */
    private static final int CORE_POOL_SIZE = 6;

    /**
     * 当线程池当前线程数量大于 {@value CORE_POOL_SIZE},
     * 且任务队列满，则创建新线程加入线程池，
     * 线程数量上限为{@value MAXIMUM_POOL_SIZE}
     */
    private static final int MAXIMUM_POOL_SIZE = 128;

    /**
     * 当线程池当前线程数量大于 {@value CORE_POOL_SIZE},
     * 超出的部分线程在空闲时间超过{@value KEEP_ALIVE}后,
     * 将被销毁，维持最小线程数量为 {@value CORE_POOL_SIZE}
     */
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "PriorityExecutor #" + mCount.getAndIncrement());
        }
    };

    private static final Comparator<Runnable> sRunnableComparator = new Comparator<Runnable>() {

        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            if (lhs instanceof PriorityRunnable && rhs instanceof PriorityRunnable) {
                return ((PriorityRunnable) lhs).priority.ordinal() - ((PriorityRunnable) rhs).priority.ordinal();
            } else {
                return 0;
            }
        }
    };

    private final ThreadPoolExecutor mThreadPoolExecutor;

    public PriorityExecutor() {
        this(CORE_POOL_SIZE);
    }

    /**
     * BlockingQueue使用无边界队列{@link PriorityBlockingQueue}，支持抢占式执行线程，
     * 不存在队满情况，线程池线程数量下限和上限均为{@value CORE_POOL_SIZE}
     * ThreadPoolExecutor创建第2，3，4个参数将无效，不存在使用场景
     */
    public PriorityExecutor(int poolSize) {
        BlockingQueue<Runnable> mPoolWorkQueue = new PriorityBlockingQueue<Runnable>(MAXIMUM_POOL_SIZE,
                sRunnableComparator);
        mThreadPoolExecutor = new ThreadPoolExecutor(poolSize, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
                mPoolWorkQueue, sThreadFactory);
    }

    public int getPoolSize() {
        return mThreadPoolExecutor.getCorePoolSize();
    }

    public void setPoolSize(int poolSize) {
        if (poolSize > 0) {
            mThreadPoolExecutor.setCorePoolSize(poolSize);
        }
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return mThreadPoolExecutor;
    }

    public boolean isBusy() {
        return getThreadPoolExecutor().getQueue().size() >= getThreadPoolExecutor().getCorePoolSize() * 2
                && mThreadPoolExecutor.getActiveCount() >= getThreadPoolExecutor().getCorePoolSize();
    }

    /**
     *
     */
    @Override
    public void execute(final Runnable runnable) {
        if (isBusy()) {
            Logger.e("Thread pool " + mThreadPoolExecutor.getActiveCount() + "+" + getThreadPoolExecutor().getQueue().size());
            //throw new RuntimeException("Thread pool is Busy");
        }

        getThreadPoolExecutor().execute(runnable);
    }

}
