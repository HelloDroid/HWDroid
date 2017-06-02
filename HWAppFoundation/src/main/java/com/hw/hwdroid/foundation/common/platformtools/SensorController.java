/**
 * @(#)SensorController.java 2013-11-23 Copyright 2013 . All
 *                           rights reserved.
 */

package com.hw.hwdroid.foundation.common.platformtools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 耳机插拔
 *
 * @author chenjian
 * @date 2013-11-23
 */

public abstract class SensorController extends BroadcastReceiver {

    /**
     * @see BroadcastReceiver#onReceive(Context,
     * Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == intent) {
            return;
        }

        Bundle extras = intent.getExtras();
        String key = "state";
        if (extras == null || !extras.containsKey(key)) {
            return;
        }

        // 没有插入耳机
        if (extras.getInt(key, 0) == 0) {
            withoutEarphone();
        }

        // headset connected
        else if (extras.getInt(key, 0) == 1) {
            insertEarphones();
        }

        onReceive();
    }

    public abstract void onReceive();

    /**
     * 插入耳机
     */
    public abstract void insertEarphones();

    /**
     * 没有插入耳机
     */
    public abstract void withoutEarphone();

}
