
package com.hw.hwdroid.foundation.common.platformtools;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;


/**
 * 蓝牙
 *
 * @author chenj
 * @date 2013-11-23
 */

public abstract class BluetoothReceiver extends BroadcastReceiver {

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == intent) {
            return;
        }

        String action = intent.getAction();

        // 发现远程设备
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // 获取发现的设备对象
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            String dName = device.getName();
            String dAddr = device.getAddress();
            int state = device.getBondState();
            BluetoothClass bluetoothClass = device.getBluetoothClass();

            Logger.e("name=%s addr=%s ", dName, dAddr);

            // 已配对的设备
            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {

            }
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
        }
    }

}
