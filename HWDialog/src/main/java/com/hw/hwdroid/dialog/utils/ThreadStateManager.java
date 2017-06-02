package com.hw.hwdroid.dialog.utils;

import java.util.HashMap;

/**
 * ThreadState状态管理器
 * <p>
 * Created by ChenJ on 16/4/5.
 */
public class ThreadStateManager {


    //记录线程状态 key为token entry为ThreadStateEnum
    private static HashMap<String, ThreadStateEnum> threadState = new HashMap<String, ThreadStateEnum>();

    //对线程进行记录
    private static ThreadStateEnum getThreadState(String key) {
        if (StringUtils.isEmptyOrNull(key)) {
            return null;
        }
        return threadState.get(key);
    }

    //判断线程 是否取消
    public static boolean isThreadStateCancel(String token) {
        ThreadStateEnum state = ThreadStateManager.getThreadState(token);
        if (state == null || state == ThreadStateEnum.cancel) {
            return true;
        }
        return false;
    }

    // 有过多异常出现导致threadState过大，则清空
    public static void setThreadState(String key, ThreadStateEnum state) {
        if (StringUtils.isEmptyOrNull(key) || state == null) {
            return;
        }
        if (state == ThreadStateEnum.cancel) {
            //KeepAliveManager.getInstance().cancelTaskByToken(key);
            removeThreadState(key);
        } else {
            synchronized (threadState) {
                threadState.put(key, state);
            }
        }
    }

    // 清除已经中断的线程记录
    public static void removeThreadState(String key) {
        if (StringUtils.isEmptyOrNull(key)) {
            return;
        }
        synchronized (threadState) {
            threadState.remove(key);
        }
    }

}
