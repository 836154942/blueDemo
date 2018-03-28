package com.example.myapplication;

import java.io.File;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements BlueToothUtils.BlueToothStatusListener, OnItemClick {
    private static int[] images = {R.mipmap.iv_connect, R.mipmap.iv_text, R.mipmap.iv_image, R.mipmap.iv_call};
    private static String[] titles = {"连接", "文字展示", "照片展示", "拨打电话"};
    private static final int REQUSET_CODE_TAKE = 0x47;
    BlueToothUtils blueToothUtils;
    //    EditText edtext;
    TextView statusTv;
    String FILE_PATH;
    RecyclerView recyclerView;
    Adapter adapter;

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
        recyclerView = (RecyclerView) findViewById(R.id.gridView);
        adapter = new Adapter(this);
        adapter.setOnItemClickListen(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        //        edtext = (EditText) findViewById(R.id.edtext);
        //        findViewById(R.id.btnSend).setOnClickListener(this);
        //        findViewById(R.id.btnGO).setOnClickListener(this);
        //        findViewById(R.id.goConnect).setOnClickListener(this);
        //        findViewById(R.id.btnGOPhoto).setOnClickListener(this);

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
            Toast.makeText(this, "文件已保存到  " + FILE_PATH, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onitemClick(int position) {
        switch (images[position]) {
            case R.mipmap.iv_connect:
                startActivity(new Intent(this, ConnectActivity.class));
                break;
            case R.mipmap.iv_text:
                startActivity(new Intent(this, ShowActivity.class));
                break;

            case R.mipmap.iv_image:
                startActivity(new Intent(this, PhotoActivity.class));
                //                blueToothUtils.clintSend(edtext.getText().toString());
                break;
            case R.mipmap.iv_call:
                startActivity(new Intent(this, CallActivity.class));
                break;
            default:
                break;
        }
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private Context context;
        OnItemClick onItemClickListen;

        public void setOnItemClickListen(OnItemClick onItemClickListen) {
            this.onItemClickListen = onItemClickListen;
        }

        public Adapter(Context context) {
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.ivImage.setImageResource(images[position]);
            holder.textView.setText(titles[position]);
            holder.ivImage.setTag(position);
            holder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListen.onitemClick((Integer) v.getTag());
                }
            });
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImage;
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
                textView = (TextView) itemView.findViewById(R.id.tvName);

            }
        }
    }
}
