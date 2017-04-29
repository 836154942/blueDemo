package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by spc on 2017/3/17.
 */

public class BlueToothUtils {
    private static final String TAG = "BlueBle";
    private static final String UUID_SERVICE = "00000000-0000-1000-8000-00805f9b34fb";

    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private List<BluetoothDevice> mList = new ArrayList(); //搜索到的设备
    private BlueReceiver receiver = new BlueReceiver(); // 搜索到的广播
    private BlueToothStatusListener mListener;

    private BluetoothSocket mBluetoothSocket;//客户端的
    private BluetoothSocket mServerSocket;//服务器器的
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x111:
                    mListener.connectServerSuccess();
                    break;
                case 0x112:
                    mListener.connectServerException();
                    break;
                case 0x113:
                    mListener.haveNewData((String) msg.obj);
                    break;
            }
        }
    };

    public interface BlueToothStatusListener {
        void startScan();//开始扫描

        void scnanDevicesOver(List<BluetoothDevice> list);//扫描结束

        void haveNewData(String data);

        void connectServerSuccess();//连接上服务器

        void connectServerException();//连接服务器失败
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public BlueToothUtils(Context mContext, BlueToothStatusListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mContext.registerReceiver(receiver, intentFilter);
    }

    //检测蓝牙是否可用
    public boolean isUseable() {
        if (mBluetoothAdapter != null) {
            return true;
        } else {
            return false;
        }
    }


    //扫描设备
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean scanDevices() {
        boolean res1 = mBluetoothAdapter.startDiscovery();
        boolean res2 = mBluetoothAdapter.startLeScan(callback);
        mListener.startScan();
        return res1 && res2;
    }

    //取消扫描
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void cancleScanDev() {
        boolean res = mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.stopLeScan(callback);
        Log.e(TAG, "取消扫描   " + res);
    }


    //得到已经配对的设备
    public Set<BluetoothDevice> bondedDevices() {
        return mBluetoothAdapter.getBondedDevices();
    }


    //开启服务器
    public void startServer() {
        new ServerThread().start();
    }

    //服务器端的线程
    private class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                BluetoothServerSocket bluetoothServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("com.example.myapplication",
                        UUID.fromString(UUID_SERVICE));
                mServerSocket = bluetoothServerSocket.accept();//阻塞，直到有蓝牙设备链接
                if (mServerSocket != null) {
                    Log.e(TAG, " 服务器 收到客户端的连接 ");
                    bluetoothServerSocket.close();
                    //  读取器客户端的发送
                    InputStream inputStream = mServerSocket.getInputStream();
                    serverReceiveData(inputStream);//死循环 服务器一直 接受 数据
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //开启客户端
    private void connectClint(final BluetoothDevice btDev) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //通过和服务器协商的uuid来进行连接
                    mBluetoothSocket = btDev.createRfcommSocketToServiceRecord(UUID.fromString(UUID_SERVICE));

                    if (mBluetoothAdapter.isDiscovering())
                        mBluetoothAdapter.cancelDiscovery();

                    if (!mBluetoothSocket.isConnected()) {
                        Log.e("blueTooth", "客户端自己 开始连接...");
                        mBluetoothSocket.connect();
                    }
                    Log.e("blueTooth", "客户端自己  已经链接");
                    mHandler.sendEmptyMessage(0x111);
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(0x112);
                    Log.e("blueTooth", "客户端自己...链接失败");
                    try {
                        mBluetoothSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void serverReceiveData(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[100];
        int bytes;
        while (true) {
            if ((bytes = inputStream.read(buffer)) > 0) {
                byte[] buf_data = new byte[bytes];
                for (int i = 0; i < bytes; i++) {
                    buf_data[i] = buffer[i];
                }
                String data = new String(buf_data);
                Message message = Message.obtain();
                message.what = 0x113;
                message.obj = data;
                mHandler.sendMessage(message);
                Log.e(TAG, "服务器收到   " + data);
            }
        }
    }


    public void createBond(final BluetoothDevice btDev) {
        if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
            //如果这个设备取消了配对，则尝试配对
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.e(TAG, "没有配对，来配对");
                btDev.createBond();
            }
        } else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
            //如果这个设备已经配对完成，则尝试连接
            Log.e(TAG, "配对过了。");
            connectClint(btDev);
        }
    }


    //客户端发送
    public void clintSend(String msg) {
        try {
            OutputStream outputStream = mBluetoothSocket.getOutputStream();
            outputStream.write(msg.getBytes());
            outputStream.flush();
            Log.e(TAG, "客户端  已经发送了  文本内容");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
        }
    };

    class BlueReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "接收到广播  ");
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (btDevice != null) {
                    Log.e(TAG, "广播  广播  Name : " + btDevice.getName() + " Address: " + btDevice.getAddress());
                    if (!mList.contains(btDevice)) {
                        mList.add(btDevice);
                    }

                } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {
                    Log.e(TAG, "### BT ACTION_BOND_STATE_CHANGED ##");

                    int cur_bond_state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
                    int previous_bond_state = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.BOND_NONE);
                    Log.e(TAG, "### cur_bond_state ##" + cur_bond_state + " ~~ previous_bond_state" + previous_bond_state);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
                Log.e(TAG, "搜索结束");
                mListener.scnanDevicesOver(mList);
            }
        }
    }

    public void unSignBroadReceive() {
        mContext.unregisterReceiver(receiver);
    }
}
