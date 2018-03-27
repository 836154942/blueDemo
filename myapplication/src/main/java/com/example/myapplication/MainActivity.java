package com.example.myapplication;


import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, BlueToothUtils.BlueToothStatusListener {
    private static final int REQUSET_CODE_TAKE = 0x47;
    BlueToothUtils blueToothUtils;
    EditText edtext;
    TextView statusTv;
    String FILE_PATH;

    private static final String TAKE_PHOTO = "take";  // 拍照

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        blueToothUtils = BlueToothUtils.getIntence();
        blueToothUtils.addListener(this);
    }

    private void initView() {
        statusTv = (TextView) findViewById(R.id.statusTv);
        edtext = (EditText) findViewById(R.id.edtext);
        findViewById(R.id.btnSend).setOnClickListener(this);
        findViewById(R.id.btnGO).setOnClickListener(this);
        findViewById(R.id.goConnect).setOnClickListener(this);
        findViewById(R.id.btnGOPhoto).setOnClickListener(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goConnect:
                startActivity(new Intent(this, ConnectActivity.class));
                break;
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startScan() {

    }

    @Override
    public void scnanDevicesOver(List<BluetoothDevice> list) {

    }

    @Override
    public void haveNewData(String data) {
        statusTv.setText("收到  " + data);
        if (TAKE_PHOTO.equals(data)) {
            takePhoto();

        }
    }


    private void takePhoto() {

        FILE_PATH = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        // 把文件地址转换成Uri格式
        Uri uri = Uri.fromFile(file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUSET_CODE_TAKE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUSET_CODE_TAKE && resultCode == RESULT_OK) {
            ToastUtils.show("文件已保存到  " + FILE_PATH);
        }
    }


    @Override
    public void connectServerSuccess() {
        statusTv.setText("连接成功");
    }

    @Override
    public void connectServerException() {

    }

    @Override
    public void startConnect() {
        statusTv.setText("开始连接");
    }
}
