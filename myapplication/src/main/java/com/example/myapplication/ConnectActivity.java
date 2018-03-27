package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spc on 2018/3/27.
 */

public class ConnectActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, BlueToothUtils.BlueToothStatusListener {

    public static final String TAG = "ConnectActivity";
    ProgressDialog progressDialog;

    BlueToothUtils blueToothUtils;
    List<BluetoothDevice> mList = new ArrayList();

    ListView listView;
    TextView setatusView;
    DeviceAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        blueToothUtils = BlueToothUtils.getIntence().init(this, this);
        initView();
        //开启服务端
        blueToothUtils.startServer();
    }

    private void initView() {
        setatusView = (TextView) findViewById(R.id.tv_status);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        findViewById(R.id.btnScan).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScan:
                Log.e(TAG, "开始扫描  " + blueToothUtils.scanDevices());//开始扫描设备
                break;
            case R.id.end:
                blueToothUtils.cancleScanDev();//取消扫描
                break;
            //            case R.id.have:
            //                Set<BluetoothDevice> bondedDevices = blueToothUtils.bondedDevices();
            //                for (BluetoothDevice item : bondedDevices) {
            //                    Log.e("遍历配对过的设备 ", "name  " + item.getName() + "    " + item.getAddress());
            //                }
            //                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.e(TAG, mList.get(i).getName());
        blueToothUtils.createBond(mList.get(i));
    }


    @Override
    public void startScan() {
        progressDialog.setMessage("正在扫描");
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
        progressDialog.show();
        setatusView.setText("正在扫描…………");
    }

    @Override
    public void scnanDevicesOver(List<BluetoothDevice> list) {
        mList = list;
        if (adapter == null) {
            adapter = new DeviceAdapter(mList, this);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        progressDialog.dismiss();
        setatusView.setText("扫描结束……");
    }

    @Override
    public void haveNewData(String data) {
        if (isFinishing())
            return;
        setatusView.setText("收到新的数据--> " + data);
    }

    @Override
    public void connectServerSuccess() {
        setatusView.setText("链接服务器成功  ");
    }

    @Override
    public void connectServerException() {
        setatusView.setText("链接服务器失败 ");
    }

    @Override
    public void startConnect() {
        setatusView.setText("开始连接");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        blueToothUtils.unSignBroadReceive();
    }
}
