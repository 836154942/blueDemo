package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener, BlueToothUtils.BlueToothStatusListener {
    private static final String TAG = "BlueBle";

    ProgressDialog progressDialog;

    BlueToothUtils blueToothUtils;
    List<BluetoothDevice> mList = new ArrayList();
    EditText edtext;
    ListView listView;
    TextView setatusView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blueToothUtils = BlueToothUtils.getIntence().init(this, this);
        initView();
        //开启服务端
        blueToothUtils.startServer();
    }

    private void initView() {
        setatusView = (TextView) findViewById(R.id.tv_status);
        edtext = (EditText) findViewById(R.id.edtext);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        findViewById(R.id.btnScan).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);
        findViewById(R.id.btnSend).setOnClickListener(this);
        findViewById(R.id.btnGO).setOnClickListener(this);
        findViewById(R.id.btnGOPhoto).setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.e(TAG, mList.get(i).getName());
        blueToothUtils.createBond(mList.get(i));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.btnGO:
                startActivity(new Intent(this, ShowActivity.class));
                break;

            case R.id.btnSend:
                blueToothUtils.clintSend(edtext.getText().toString());
                break;
            case R.id.btnGOPhoto:
                startActivity(new Intent(this, PhotoActivity.class));
                break;
        }
    }

    @Override
    public void startScan() {
        progressDialog.setMessage("正在扫描");
        progressDialog.show();
        setatusView.setText("正在扫描…………");
    }

    @Override
    public void scnanDevicesOver(List<BluetoothDevice> list) {
        mList = list;
        listView.setAdapter(new DeviceAdapter(mList, this));
        progressDialog.dismiss();
        setatusView.setText("扫描结束……");
    }

    @Override
    public void haveNewData(String data) {
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
        blueToothUtils = null;
    }
}
