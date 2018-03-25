package com.clj.fastble.scan;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈利健 on 2016/9/2.
 * 一段限制时间内搜索所有设备
 */
public abstract class ListScanCallback extends PeriodScanCallback {
    String TAG = "ListScanCallback";
    Context context;

    /**
     * 所有被发现的设备集合
     */
    private List<BluetoothDevice> deviceList = new ArrayList<>();

    public ListScanCallback(long timeoutMillis, Context context) {
        super(timeoutMillis);
        this.context = context;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(blueReceiver, intentFilter);
    }


    BroadcastReceiver blueReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {


                BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (btDevice != null) {
                    Log.e(TAG, "广播  广播  Name : " + btDevice.getName() + " Address: " + btDevice.getAddress());

                    if (!deviceList.contains(btDevice)) {
                        deviceList.add(btDevice);
                    }


                } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {
                    Log.e(TAG, "### BT ACTION_BOND_STATE_CHANGED ##");

                    int cur_bond_state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
                    int previous_bond_state = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.BOND_NONE);


                    Log.e(TAG, "### cur_bond_state ##" + cur_bond_state + " ~~ previous_bond_state" + previous_bond_state);
                }
            }
        }

    };

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (device == null)
            return;

        if (!deviceList.contains(device)) {
            deviceList.add(device);
        }
    }

    @Override
    public void onScanTimeout() {
        BluetoothDevice[] devices = new BluetoothDevice[deviceList.size()];
        for (int i = 0; i < devices.length; i++) {
            devices[i] = deviceList.get(i);
        }
        onDeviceFound(devices);
        unseigReceiver();
    }

    public void unseigReceiver() {
        context.unregisterReceiver(blueReceiver);
    }

    public abstract void onDeviceFound(BluetoothDevice[] devices);

}
