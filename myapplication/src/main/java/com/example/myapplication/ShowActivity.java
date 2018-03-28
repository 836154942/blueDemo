package com.example.myapplication;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by shipinchao on 2018/3/13.
 */

public class ShowActivity extends AppCompatActivity implements BlueToothUtils.BlueToothStatusListener {

    TextView mTv;
    ScrollView scrollView;
    private static final String sdCard = android.os.Environment.getExternalStorageDirectory().getPath();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        mTv = (TextView) findViewById(R.id.tv01);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        BlueToothUtils.getIntence().addListener(this);
        Log.e(sdCard + "", " ");

    }

    @Override
    public void startScan() {

    }

    @Override
    public void scnanDevicesOver(List<BluetoothDevice> list) {
        Log.e("@222222222222222", "scnanDevicesOver");
    }

    @Override
    public void haveNewData(String data) {
        if (isFinishing()) {
            return;
        }
        Log.e("@222222222222222", "有新的 数据    " + data);
        if (!mTv.getText().equals("")) {
            mTv.append("\r\n");
        }

        mTv.append(data);
        ToastUtils.show(data);
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
    }

    @Override
    public void connectServerSuccess() {

    }

    @Override
    public void connectServerException() {

    }

    @Override
    public void startConnect() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BlueToothUtils.getIntence().remove(this);
    }
}
