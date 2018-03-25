package com.example.myapplication;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shipinchao on 2018/3/24.
 */

public class PhotoActivity extends AppCompatActivity implements BlueToothUtils.BlueToothStatusListener {
    private int[] photoId = {R.mipmap.bg1, R.mipmap.bg2, R.mipmap.bg3,
            R.mipmap.bg5, R.mipmap.bg6};
    ViewPager viewPager;
    private int currentIndex;

    private static final String RIGHT_STRING = "r";
    private static final String LEFT_STRING = "l";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initView();
        BlueToothUtils.getIntence().addListener(this);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = new PhotoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(PhotoFragment.EXTRA_RESID, photoId[position]);
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return photoId.length;
            }
        });
    }

    @Override
    public void startScan() {

    }

    @Override
    public void scnanDevicesOver(List<BluetoothDevice> list) {

    }

    @Override
    public void haveNewData(String data) {

        if (isFinishing()) {
            return;
        }

        ToastUtils.show("收到   " + data);
        if (RIGHT_STRING.equals(data)) {
            if (currentIndex == photoId.length - 1) {
                currentIndex = -1;
            }
            viewPager.setCurrentItem(++currentIndex);
        } else if (LEFT_STRING.equals(data)) {

            if (currentIndex == 0) {
                currentIndex = photoId.length;
            }
            viewPager.setCurrentItem(--currentIndex);
        }

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
    protected void onDestroy() {
        super.onDestroy();
        BlueToothUtils.getIntence().remove(this);
    }
}
