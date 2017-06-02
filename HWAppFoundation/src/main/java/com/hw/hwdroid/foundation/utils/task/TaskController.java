package com.hw.hwdroid.foundation.utils.task;

/**
 * 线程池管理类
 */
public class TaskController {

    private PriorityExecutor executor;

    public static TaskController get() {
        return ControllerHolder.INSTANCE;
    }

    private TaskController() {
        executor = new PriorityExecutor();
    }

    public void executeRunnableOnThread(Runnable runnable) {
        executeRunnableOnThread(new PriorityRunnable(runnable));
    }

    public void executeRunnableOnThread(PriorityRunnable runnable) {
        executor.execute(runnable);
    }

    private static class ControllerHolder {
        private static final TaskController INSTANCE = new TaskController();
    }

}
